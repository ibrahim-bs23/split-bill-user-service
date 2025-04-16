package com.brainstation23.skeleton.common.mapper;

import com.brainstation23.skeleton.data.entity.ApplicationSetting;
import com.brainstation23.skeleton.presenter.domain.request.ApplicationSettingAddRequest;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingResponse;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsDetailsResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-16T17:24:40+0600",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class ApplicationSettingMapperImpl implements ApplicationSettingMapper {

    @Override
    public ApplicationSetting mapRequestToEntity(ApplicationSettingAddRequest request) {
        if ( request == null ) {
            return null;
        }

        ApplicationSetting applicationSetting = new ApplicationSetting();

        applicationSetting.setSettingName( request.getSettingName() );
        applicationSetting.setSettingCode( request.getSettingCode() );
        applicationSetting.setSettingValue( request.getSettingValue() );
        if ( request.getDataType() != null ) {
            applicationSetting.setDataType( request.getDataType() );
        }
        applicationSetting.setDescription( request.getDescription() );

        return applicationSetting;
    }

    @Override
    public ApplicationSettingResponse mapToResponse(ApplicationSetting entity) {
        if ( entity == null ) {
            return null;
        }

        ApplicationSettingResponse applicationSettingResponse = new ApplicationSettingResponse();

        applicationSettingResponse.setId( entity.getId() );
        applicationSettingResponse.setSettingName( entity.getSettingName() );
        applicationSettingResponse.setSettingCode( entity.getSettingCode() );
        applicationSettingResponse.setSettingValue( entity.getSettingValue() );
        applicationSettingResponse.setDescription( entity.getDescription() );
        applicationSettingResponse.setDataType( entity.getDataType() );
        applicationSettingResponse.setIsActive( entity.getIsActive() );

        return applicationSettingResponse;
    }

    @Override
    public ApplicationSettingsDetailsResponse mapToResponseForDetails(ApplicationSetting entity) {
        if ( entity == null ) {
            return null;
        }

        ApplicationSettingsDetailsResponse.ApplicationSettingsDetailsResponseBuilder applicationSettingsDetailsResponse = ApplicationSettingsDetailsResponse.builder();

        applicationSettingsDetailsResponse.settingName( entity.getSettingName() );
        applicationSettingsDetailsResponse.settingCode( entity.getSettingCode() );
        applicationSettingsDetailsResponse.settingValue( entity.getSettingValue() );
        applicationSettingsDetailsResponse.dataType( entity.getDataType() );
        applicationSettingsDetailsResponse.description( entity.getDescription() );
        applicationSettingsDetailsResponse.createdBy( entity.getCreatedBy() );
        applicationSettingsDetailsResponse.updatedBy( entity.getUpdatedBy() );
        applicationSettingsDetailsResponse.createdDate( entity.getCreatedDate() );
        applicationSettingsDetailsResponse.updatedDate( entity.getUpdatedDate() );
        applicationSettingsDetailsResponse.isActive( entity.getIsActive() );

        return applicationSettingsDetailsResponse.build();
    }
}
