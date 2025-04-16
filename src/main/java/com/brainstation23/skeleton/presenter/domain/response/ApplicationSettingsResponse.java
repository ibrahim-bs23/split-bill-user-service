package com.brainstation23.skeleton.presenter.domain.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSettingsResponse implements Serializable {
    private String settingsCode;
    private String settingsName;
    private String settingsValue;
}
