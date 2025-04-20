package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(SignUpRequest request);

    TokenResponse authenticate(AuthenticationRequest request);




}
