package com.brainstation23.skeleton.presenter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsValueDto implements Serializable {

    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    private String value;
}
