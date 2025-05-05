package com.brainstation23.skeleton.core.domain.enums;

public enum ApplicationSettingsCode {

    EXAMPLE_APP_SETTINGS("1001", "Example app settings."),
    JWT_TOKEN_LIVE_MIN("1002", "JWT_TOKEN_LIVE_MIN)")
    ;

    private final String code;
    private final String name;

    ApplicationSettingsCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
