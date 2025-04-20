package com.brainstation23.skeleton.presenter.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TfaVerificationRequest {
    @NotBlank(message = "please.provide.feature.code")
    private String globalFeatureCode;

    @NotBlank(message = "please.provide.identifier.value")
    private String identifierValue;

    private String tfaSessionId;
}
