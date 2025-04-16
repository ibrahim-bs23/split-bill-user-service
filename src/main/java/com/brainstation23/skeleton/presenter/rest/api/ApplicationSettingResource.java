package com.brainstation23.skeleton.presenter.rest.api;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.service.ApplicationSettingsService;
import com.brainstation23.skeleton.presenter.annotations.AuthorizeScope;
import com.brainstation23.skeleton.presenter.domain.request.ApplicationSettingAddRequest;
import com.brainstation23.skeleton.presenter.domain.request.ApplicationSettingUpdateRequest;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsDetailsResponse;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsJsonResponse;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsResponse;
import com.brainstation23.skeleton.presenter.domain.response.PaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/application-setting")
@RequiredArgsConstructor
public class ApplicationSettingResource extends BaseResource {

    private final ApplicationSettingsService applicationSettingsService;

    @GetMapping("/all")
    public ApiResponse<PaginationResponse<ApplicationSettingsDetailsResponse>> getAllApplicationSetting(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String settingName,
            @RequestParam(required = false) String settingCode,
            @RequestParam(required = false) Boolean isActive) {

        final PaginationResponse<ApplicationSettingsDetailsResponse> applicationSettingResponse =
                applicationSettingsService.getAllApplicationSetting(pageNumber, pageSize, sortBy, sortOrder, settingName, settingCode, isActive);

        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), applicationSettingResponse);
    }

    @PostMapping("/add")
    @AuthorizeScope(scopes = {"ADMIN_APPLICATION_SETTING"})
    public ApiResponse<ApplicationSettingsDetailsResponse> saveApplicationSetting(@Valid @RequestBody ApplicationSettingAddRequest request) {
        final ApplicationSettingsDetailsResponse response =
                applicationSettingsService.saveApplicationSetting(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @PostMapping("/edit")
    public ApiResponse<ApplicationSettingsDetailsResponse> updateApplicationSetting(@Valid @RequestBody ApplicationSettingUpdateRequest request) {
        final ApplicationSettingsDetailsResponse response =
                applicationSettingsService.updateApplicationSetting(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @GetMapping("/details/{settingCode}")
    public ApiResponse<ApplicationSettingsDetailsResponse> getApplicationSettingDetails(@PathVariable("settingCode") String settingCode) {
        final ApplicationSettingsDetailsResponse applicationSettingResponse =
                applicationSettingsService.getApplicationSettingDetails(settingCode);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), applicationSettingResponse);
    }

    @GetMapping("/code/{settingCode}")
    public ApiResponse<ApplicationSettingsResponse> getApplicationSettings(@PathVariable("settingCode") String settingCode) {

        final ApplicationSettingsResponse applicationSettingResponse =
                applicationSettingsService.getApplicationSettings(settingCode);

        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), applicationSettingResponse);
    }

    @GetMapping("/format/code/{settingCode}")
    public ApiResponse<ApplicationSettingsJsonResponse> getApplicationSettingsJson(@PathVariable("settingCode") String settingCode) {

        final ApplicationSettingsJsonResponse applicationSettingsJsonResponse =
                applicationSettingsService.getFormatApplicationSettings(settingCode);

        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), applicationSettingsJsonResponse);
    }


}
