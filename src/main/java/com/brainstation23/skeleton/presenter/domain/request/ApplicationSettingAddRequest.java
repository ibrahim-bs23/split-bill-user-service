package com.brainstation23.skeleton.presenter.domain.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Md. Himon Shekh
 * Date: 12/20/2023 12:42 PM
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSettingAddRequest {

    @NotEmpty(message = "Settings name can not be empty")
    private String settingName;

    @NotEmpty(message = "Settings code can not be empty")
    private String settingCode;

    @NotEmpty(message = "Settings value can not be empty")
    private String settingValue;

    @NotEmpty(message = "Description can not be empty")
    private String description;

    @NotNull(message = "Data type can not be empty")
    private Integer dataType;

}