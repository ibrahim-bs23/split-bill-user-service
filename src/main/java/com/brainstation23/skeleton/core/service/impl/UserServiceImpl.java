package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.common.mapper.UserMapper;
import com.brainstation23.skeleton.common.utils.RandomGenerator;
import com.brainstation23.skeleton.core.domain.enums.ConnectionStatus;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.*;
import com.brainstation23.skeleton.core.domain.model.CurrentUserContext;
import com.brainstation23.skeleton.core.domain.model.UserJwtPayload;
import com.brainstation23.skeleton.core.jwt.service.AuthServiceImpl;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.UserService;
import com.brainstation23.skeleton.data.entity.user.Connection;
import com.brainstation23.skeleton.data.entity.user.Users;
import com.brainstation23.skeleton.data.repository.user.ConnectionRepository;
import com.brainstation23.skeleton.data.repository.user.UserRepository;
import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.devicebinding.DeviceInfoRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.ConnectionUpdate;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectionRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceImpl authService;
    private final ConnectionRepository connectionRepository;

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

    @Override
    public List<UserResponseDTO> getUserByName(String userName) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Users> users = userRepository.findUsersByUsernameLike("%" + userName + "%", pageable);
        if (users.isEmpty()) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void sendConnectionRequest(String receiverId) {
        validateReceiverId(receiverId);
        validateSelfConnection(receiverId);
        createConnectionRequest(receiverId);
    }

    @Override
    public List<String> getPendingConnectionRequests() {
        String user=getCurrentUserContext().getUserName();
        List<Connection> pendingConnections = connectionRepository.findByConnectedUserAndConnectionStatus(user, ConnectionStatus.PENDING);
        return pendingConnections.stream()
                .map(Connection::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public void acceptRequest(ConnectionUpdate connectionUpdate) {
        String senderUserName=getCurrentUserContext().getUserName();
        Connection connection = connectionRepository.findByUserNameAndConnectedUserAndConnectionStatus(connectionUpdate.getConnectedUser(),senderUserName,ConnectionStatus.PENDING).orElseThrow(
                        ()->new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
                );
        connection.setConnectionStatus(Objects.equals(connectionUpdate.getConnectionStatus(), ConnectionStatus.CONNECTED.toString()) ? ConnectionStatus.CONNECTED : ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
    }

    @Override
    public void unfriendUser(String receiverName) {
        String senderUserName=getCurrentUserContext().getUserName();
        Optional<Connection> directConnection = connectionRepository.findByUserNameAndConnectedUser(senderUserName,receiverName );
        Optional<Connection> byDirectionalConnection = connectionRepository.findByUserNameAndConnectedUser(receiverName, senderUserName);
        if(directConnection.isPresent()){
            directConnection.get().setConnectionStatus(ConnectionStatus.UNFRIENDED);
            connectionRepository.save(directConnection.get());
        }
        else if(byDirectionalConnection.isPresent()){
            byDirectionalConnection.get().setConnectionStatus(ConnectionStatus.UNFRIENDED);
            connectionRepository.save(byDirectionalConnection.get());
        }
        else {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

    }

    @Override
    public List<String> getUserFriends() {
        String user=getCurrentUserContext().getUserName();
        List<Connection> connections = connectionRepository.findByUserNameOrConnectedUserAndConnectionStatus(user, user,ConnectionStatus.CONNECTED);
        return connections.stream()
                .map(connection -> connection.getConnectedUser().equals(user) ? connection.getUserName() : connection.getConnectedUser())
                .collect(Collectors.toList());
    }

    private void createConnectionRequest(String receiver) {
        String senderUserName=getCurrentUserContext().getUserName();
        Connection connection = createConnection(senderUserName,receiver);
        connectionRepository.save(connection);
    }

    private Connection createConnection(String senderUserName, String receiver) {
        Optional<Connection> connection = connectionRepository.findByUserNameAndConnectedUser(senderUserName,receiver);
        connection.ifPresent(connection1 -> connection1.setConnectionStatus(ConnectionStatus.PENDING));
        return connection.orElseGet(() -> Connection.builder()
                .userName(senderUserName)
                .connectedUser(receiver)
                .connectionStatus(ConnectionStatus.PENDING)
                .connectedAt(LocalDateTime.now())
                .build());
    }

    private void validateReceiverId(String receiverId) {
        if(StringUtils.isBlank(receiverId)){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        userRepository.findByUsername(receiverId).orElseThrow(
                ()->new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
        String senderUserName=getCurrentUserContext().getUserName();
        connectionRepository.findByUserNameAndConnectedUserAndConnectionStatusIn(senderUserName, receiverId,List.of(ConnectionStatus.PENDING, ConnectionStatus.CONNECTED)).ifPresent(
                connection -> { throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA); }
        );
    }

    private void validateSelfConnection(String receiverId) {
        CurrentUserContext currentUserContext=getCurrentUserContext();
        if(currentUserContext.getUserName().equals(receiverId)){
            throw new UnauthorizedResourceException(ResponseMessage.UNAUTHORIZED_CONNECTION_REQUEST);
        }
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