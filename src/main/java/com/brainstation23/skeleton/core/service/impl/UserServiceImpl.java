package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.common.mapper.UserMapper;
import com.brainstation23.skeleton.common.utils.RandomGenerator;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.AlreadyExists;
import com.brainstation23.skeleton.core.domain.exceptions.AuthenticationFailedException;
import com.brainstation23.skeleton.core.domain.model.UserJwtPayload;
import com.brainstation23.skeleton.core.jwt.service.AuthServiceImpl;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.UserService;
import com.brainstation23.skeleton.data.entity.user.Users;
import com.brainstation23.skeleton.data.repository.user.UserRepository;
import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.devicebinding.DeviceInfoRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceImpl authService;

    @Override
    public UserResponseDTO registerUser(SignUpRequest request) {
        validateRequest(request);
        Users users=saveNewUsers(request);
        return userMapper.toUserResponseDTO(users);
    }

    @Override
    public TokenResponse authenticate(AuthenticationRequest request) {

        String username = request.getUserName();
        String password = request.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AuthenticationFailedException(ResponseMessage.UNMATCHED_USERNAME_OR_PASSWORD);
        }
        Users users=getUser(request);
        validatePassword(request,users);
        return handleActiveUser(request, users);
    }

    private TokenResponse handleActiveUser(AuthenticationRequest request, Users users) {
        UserJwtPayload userJwtPayload = authService.prepareAccessJwtPayload(users);
        String accessToken = authService.generateAccessToken(userJwtPayload);
        return new TokenResponse(accessToken);
    }

    private void validatePassword(AuthenticationRequest request, Users users) {
        if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
           throw new AuthenticationFailedException(ResponseMessage.INVALID_PASSWORD);
        }
    }


    private Users getUser(AuthenticationRequest request) {
        return userRepository.findByUsername(request.getUserName()).orElseThrow(
                () -> new AuthenticationFailedException(ResponseMessage.UNMATCHED_USERNAME_OR_PASSWORD)
        );
    }

    private Users saveNewUsers(SignUpRequest request) {
        Users users = createNewUsers(request);
        return userRepository.save(users);
    }

    private Users createNewUsers(SignUpRequest request) {
        Users users = userMapper.toUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        return users;
    }

    private void validateRequest(SignUpRequest request) {
        validateUserName(request.getUsername());
        validateUserEmail(request.getEmail());
    }

    private void validateUserEmail(String email) {
        Optional<Users> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            throw new AlreadyExists(ResponseMessage.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateUserName(String username) {
        Optional<Users> byUsername = userRepository.findByUsername(username);
        if(byUsername.isPresent()) {
            throw new AlreadyExists(ResponseMessage.USER_NAME_ALREADY_EXISTS);
        }
    }
    private String generateRandomPassword() {
        return RandomGenerator.generateRandomPassword();
    }
}
