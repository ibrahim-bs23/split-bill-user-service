package com.brainstation23.skeleton.presenter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Author: Md. Himon Shekh
 * Date: 12/20/2023 12:42 PM
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSettingResponse implements Serializable {

    private Long id;

    private String settingName;

    private String settingCode;

    private String settingValue;

    private String description;

    private Integer dataType;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastUpdatedDate;

    private Boolean isActive = Boolean.TRUE;
}
