package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.request.ValidateUserEmail;
import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.ConnectionUpdate;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO registerUser(SignUpRequest request);

    TokenResponse authenticate(AuthenticationRequest request);

    List<UserResponseDTO> getUserByName(String userName);

    void sendConnectionRequest( String receiverId);

    List<String> getPendingConnectionRequests();

    void acceptRequest(ConnectionUpdate connectionUpdate);

    void unfriendUser(String receiverName);

    List<String> getUserFriends();

    void validateUser(ValidateUserEmail verificationCode);

    UserResponseDTO getUserDetails(Long id);

    Boolean verifyConnection(String userName);
}
