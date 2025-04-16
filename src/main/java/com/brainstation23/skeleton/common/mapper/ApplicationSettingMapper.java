package com.brainstation23.skeleton.common.mapper;

import com.brainstation23.skeleton.data.entity.ApplicationSetting;
import com.brainstation23.skeleton.presenter.domain.request.ApplicationSettingAddRequest;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingResponse;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsDetailsResponse;
import org.mapstruct.Mapper;

/**
 * Author: Md. Himon Shekh
 * Date: 12/20/2023 12:42 PM
 */

@Mapper(componentModel = "spring")
public interface ApplicationSettingMapper {

    ApplicationSetting mapRequestToEntity(final ApplicationSettingAddRequest request);

    ApplicationSettingResponse mapToResponse(final ApplicationSetting entity);
    ApplicationSettingsDetailsResponse mapToResponseForDetails(final ApplicationSetting entity);
}
