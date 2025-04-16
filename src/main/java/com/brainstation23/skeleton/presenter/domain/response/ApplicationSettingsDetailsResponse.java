package com.brainstation23.skeleton.presenter.domain.response;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationSettingsDetailsResponse implements Serializable {

    private String settingName;

    private String settingCode;

    private String settingValue;

    private Integer dataType;

    private String description;

    private String createdBy;

    private String updatedBy;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date updatedDate;

    private Boolean isActive;
}
