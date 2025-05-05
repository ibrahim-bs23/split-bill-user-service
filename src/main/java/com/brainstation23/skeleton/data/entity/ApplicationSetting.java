package com.brainstation23.skeleton.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "APPLICATION_SETTING",schema = "user_service")
public class ApplicationSetting extends BaseEntity {

    @Column(name = "SETTING_NAME")
    private String settingName;

    @Column(name = "SETTING_CODE")
    private String settingCode;

    @Column(name = "SETTING_VALUE", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "DATA_TYPE", nullable = false)
    private int dataType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COUNTRY_ID")
    private Long countryId;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted = Boolean.FALSE;

}
