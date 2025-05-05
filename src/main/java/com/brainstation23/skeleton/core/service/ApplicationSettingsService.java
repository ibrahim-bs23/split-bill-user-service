package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.common.mapper.ApplicationSettingMapper;
import com.brainstation23.skeleton.common.utils.PageUtils;
import com.brainstation23.skeleton.core.domain.enums.ApplicationSettingsCode;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.model.CurrentUserContext;
import com.brainstation23.skeleton.data.entity.ApplicationSetting;
import com.brainstation23.skeleton.presenter.domain.request.ApplicationSettingAddRequest;
import com.brainstation23.skeleton.presenter.domain.request.ApplicationSettingUpdateRequest;
import com.brainstation23.skeleton.presenter.domain.request.PaginationRequest;
import com.brainstation23.skeleton.presenter.domain.response.*;
import com.brainstation23.skeleton.data.repository.ApplicationSettingsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationSettingsService extends BaseService {

    private final ApplicationSettingsRepository applicationSettingsRepository;
    private final ApplicationSettingMapper applicationSettingMapper;

    public PaginationResponse<ApplicationSettingsDetailsResponse> getAllApplicationSetting(final Integer pageNumber, final Integer pageSize, final String sortBy, final String sortOrder, final String settingName, final String settingCode, final Boolean isActive) {
        final PaginationRequest paginationRequest = PageUtils.mapToPaginationRequest(pageNumber, pageSize, sortBy, sortOrder);
        final Pageable pageable = PageUtils.getPageable(paginationRequest);

        final Page<ApplicationSettingsDetailsResponse> page = applicationSettingsRepository.findAllByParam(
                pageable,
                StringUtils.isEmpty(settingName) ? null : settingName,
                StringUtils.isEmpty(settingCode) ? null : settingCode,
                Objects.isNull(isActive) ? null : isActive
        ).map(applicationSettingMapper::mapToResponseForDetails);

        return page.getContent().isEmpty() ?
                PageUtils.mapToPaginationResponseDto(Page.empty(), paginationRequest) :
                PageUtils.mapToPaginationResponseDto(page, paginationRequest);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ApplicationSettingsDetailsResponse saveApplicationSetting(final ApplicationSettingAddRequest request) {
        final CurrentUserContext currentUserContext = getCurrentUserContext();

        Optional<ApplicationSetting> applicationSettingOptional = getApplicationSettingByCode(request.getSettingCode());
        if (applicationSettingOptional.isPresent()) {
            throw new InvalidRequestDataException(ResponseMessage.RECORD_ALREADY_EXIST.getResponseMessage());
        }

        final ApplicationSetting applicationSetting = applicationSettingMapper.mapRequestToEntity(request);
        applicationSetting.setSettingCode(request.getSettingCode());
        applicationSetting.setIsActive(Boolean.TRUE);
        applicationSetting.setCreatedBy(currentUserContext.getUserIdentity());
        applicationSetting.setCreatedDate(getCurrentDate());

        applicationSettingsRepository.save(applicationSetting);

        return applicationSettingMapper.mapToResponseForDetails(applicationSetting);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ApplicationSettingsDetailsResponse updateApplicationSetting(final ApplicationSettingUpdateRequest request) {

        final CurrentUserContext currentUserContext = getCurrentUserContext();
        final Optional<ApplicationSetting> applicationSettingOptional = getApplicationSettingByCode(request.getSettingCode());

        if (applicationSettingOptional.isEmpty()) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());
        }

        updateApplicationSettings(applicationSettingOptional.get(), request, currentUserContext.getUserIdentity());

        return applicationSettingMapper.mapToResponseForDetails(applicationSettingOptional.get());
    }

    public ApplicationSettingsDetailsResponse getApplicationSettingDetails(final String settingCode) {
        final Optional<ApplicationSetting> applicationSettingOptional = getApplicationSettingByCode(settingCode);

        if (applicationSettingOptional.isEmpty()) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());
        }

        return applicationSettingMapper.mapToResponseForDetails(applicationSettingOptional.get());
    }

    public ApplicationSettingsResponse getApplicationSettings(final String settingCode) {

        return applicationSettingsRepository.findBySettingCode(settingCode)
                .map(applicationSetting -> ApplicationSettingsResponse.builder()
                        .settingsName(applicationSetting.getSettingName())
                        .settingsCode(applicationSetting.getSettingCode())
                        .settingsValue(applicationSetting.getSettingValue())
                        .build()).orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage()));
    }

    public ApplicationSettingsJsonResponse getFormatApplicationSettings(final String settingCode) {
        final ApplicationSettingsResponse applicationSettingsResponse = getApplicationSettings(settingCode);

        try {
            Map<String, String> map = objectMapper.readValue(applicationSettingsResponse.getSettingsValue(), new TypeReference<>() {
            });
            final List<SettingsValueDto> settingsValues = map.entrySet().stream().map(this::prepareSettingKeyValue).toList();
            return ApplicationSettingsJsonResponse.builder()
                    .settingsName(applicationSettingsResponse.getSettingsName())
                    .settingsCode(applicationSettingsResponse.getSettingsCode())
                    .settingsValue(settingsValues)
                    .build();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new InvalidRequestDataException(ResponseMessage.JSON_PARSE_ERROR.getResponseMessage());
        }
    }

    public ApplicationSettingsResponse getApplicationSettingsValue(ApplicationSettingsCode applicationSettingsCode) {
        return applicationSettingsRepository.getApplicationSettingByCode(applicationSettingsCode.getCode());
    }

    public Integer getExampleAppSettings() {
        ApplicationSettingsResponse response = getApplicationSettingsValue(ApplicationSettingsCode.EXAMPLE_APP_SETTINGS);
        return Integer.parseInt(response.getSettingsValue());
    }


    public Optional<ApplicationSetting> getApplicationSettingByCode(final String settingCode) {
        return applicationSettingsRepository.findBySettingCode(settingCode);
    }

    private void updateApplicationSettings(final ApplicationSetting applicationSetting,
                                           final ApplicationSettingUpdateRequest request,
                                           final String userIdentity) {
        applicationSetting.setSettingName(request.getSettingName());
        applicationSetting.setSettingValue(request.getSettingValue());
        applicationSetting.setDataType(request.getDataType());
        applicationSetting.setDescription(request.getDescription());
        applicationSetting.setIsActive(request.getIsActive());
        applicationSetting.setUpdatedBy(userIdentity);
        applicationSetting.setUpdatedDate(getCurrentDate());

        applicationSettingsRepository.save(applicationSetting);
    }

    private SettingsValueDto prepareSettingKeyValue(Map.Entry<String, String> mapEntry) {
        return SettingsValueDto.builder()
                .key(mapEntry.getKey())
                .value(mapEntry.getValue())
                .build();
    }

}
