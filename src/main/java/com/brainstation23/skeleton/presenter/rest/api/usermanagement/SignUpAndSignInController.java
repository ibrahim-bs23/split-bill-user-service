package com.brainstation23.skeleton.presenter.rest.api.usermanagement;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.domain.request.ValidateUserEmail;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.UserService;
import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL)
public class SignUpAndSignInController extends BaseService {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> registerUser(@Valid @RequestBody SignUpRequest request) {
        UserResponseDTO response = userService.registerUser(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> loginUser(@Valid @RequestBody AuthenticationRequest request) {
        TokenResponse response = userService.authenticate(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @PutMapping("/validate")
    public ApiResponse<Void> validateUser(@Valid @RequestBody ValidateUserEmail request) {
        userService.validateUser(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL));
    }

}
