package com.brainstation23.skeleton.presenter.domain.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class TokenResponse {
    private String accessToken;
    private Boolean isActive;
}
