package com.brainstation23.skeleton.presenter.domain.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationSettingsJsonResponse implements Serializable {

    private String settingsCode;
    private String settingsName;
    private List<SettingsValueDto> settingsValue;
}
