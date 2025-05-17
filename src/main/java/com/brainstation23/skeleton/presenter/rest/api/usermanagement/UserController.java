package com.brainstation23.skeleton.presenter.rest.api.usermanagement;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.UserService;
import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.ConnectionUpdate;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL+"/management")
public class UserController extends BaseService {
    private final UserService userService;

    @GetMapping("/{userName}")
    public ApiResponse<List<UserResponseDTO>> getUserDetails(@PathVariable String userName) {
        List<UserResponseDTO> response = userService.getUserByName(userName);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @PostMapping("/sendConnectionRequest/{receiverName}")
    public ApiResponse<?> sendConnectionRequest(@PathVariable String receiverName) {
        userService.sendConnectionRequest(receiverName);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @GetMapping("/getPendingConnectionRequests")
    public ApiResponse<List<String>> getPendingConnectionRequests() {
        List<String> response = userService.getPendingConnectionRequests();
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @PutMapping("/updatePendingConnectionRequests")
    public ApiResponse<?> updateConnectionStatus(@RequestBody @Valid ConnectionUpdate connectionUpdate) {
        userService.acceptRequest(connectionUpdate);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @PutMapping("/unfriendUser/{receiverName}")
    public ApiResponse<?> unFriendUsers(@PathVariable String receiverName) {
        userService.unfriendUser(receiverName);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @GetMapping("/getUserFriends")
    public ApiResponse<List<String>> getUserFriends() {
        List<String> response = userService.getUserFriends();
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @GetMapping("/user/details/{id}")
    public ApiResponse<UserResponseDTO> getUserDetails(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserDetails(id);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @GetMapping("/verify-connection/{userName}")
    public ApiResponse<Boolean> verifyConnection(@PathVariable String userName) {
        Boolean response = userService.verifyConnection(userName);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

}
