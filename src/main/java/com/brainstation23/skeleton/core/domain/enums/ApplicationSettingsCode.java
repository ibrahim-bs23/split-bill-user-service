package com.brainstation23.skeleton.core.domain.enums;

public enum ApplicationSettingsCode {

    EXAMPLE_APP_SETTINGS("1001", "Example app settings."),
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
