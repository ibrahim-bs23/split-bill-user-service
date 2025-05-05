package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.common.mapper.UserMapper;
import com.brainstation23.skeleton.common.utils.ChecksumUtil;
import com.brainstation23.skeleton.common.utils.JWTUtils;
import com.brainstation23.skeleton.common.utils.RandomGenerator;
import com.brainstation23.skeleton.common.utils.SessionIdUtil;
import com.brainstation23.skeleton.core.domain.enums.ApplicationSettingsCode;
import com.brainstation23.skeleton.core.domain.enums.ConnectionStatus;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.*;
import com.brainstation23.skeleton.core.domain.model.CurrentUserContext;
import com.brainstation23.skeleton.core.domain.model.UserJwtPayload;
import com.brainstation23.skeleton.core.domain.request.ValidateUserEmail;
import com.brainstation23.skeleton.core.domain.response.AccessTokenResponse;
import com.brainstation23.skeleton.core.jwt.service.AuthServiceImpl;
import com.brainstation23.skeleton.core.service.ApplicationSettingsService;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.RedisService;
import com.brainstation23.skeleton.core.service.UserService;
import com.brainstation23.skeleton.data.entity.ApplicationSetting;
import com.brainstation23.skeleton.data.entity.redis.RedisAccessToken;
import com.brainstation23.skeleton.data.entity.user.Connection;
import com.brainstation23.skeleton.data.entity.user.Users;
import com.brainstation23.skeleton.data.repository.redis.RedisSessionIdRepository;
import com.brainstation23.skeleton.data.repository.redis.RedisTokenRepository;
import com.brainstation23.skeleton.data.repository.user.ConnectionRepository;
import com.brainstation23.skeleton.data.repository.user.UserRepository;
import com.brainstation23.skeleton.presenter.context.UserContextHolder;
import com.brainstation23.skeleton.presenter.domain.request.auth.AuthenticationRequest;
import com.brainstation23.skeleton.presenter.domain.request.devicebinding.DeviceInfoRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.ConnectionUpdate;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.auth.TokenResponse;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectionRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceImpl authService;
    private final ConnectionRepository connectionRepository;
    private final EmailService emailService;
    private final RedisTokenRepository redisTokenRepository;
    private final RedisSessionIdRepository redisSessionIdRepository;
    private final ApplicationSettingsService applicationSettingsService;
    private final RedisService redisService;

    @Value("${jwt.token.expiry.minute}")
    protected String jwtExpiryTime;
    @Value("${jwt.secret.key}")
    protected String jwtSecretKey;

    @Transactional
    @Override
    public UserResponseDTO registerUser(SignUpRequest request) {
        validateRequest(request);
        Users users = saveNewUsers(request);
        sendVerificationEmail(users);
        return userMapper.toUserResponseDTO(users);
    }

    @Transactional
    public void sendVerificationEmail(Users users) {
        String toEmail = users.getEmail();
        String subject = "Email Verification for Your Account";

        String body =
                "Dear " + users.getUsername() + ",\n\n" +
                        "Thank you for registering with Split Pay! To complete your registration and confirm your email address, please use the following verification code:\n\n" +
                        "Verification Code: " + users.getVerificationCode() + "\n\n" +
                        "Please note, this verification code will remain valid for the next 15 minutes. If you did not request this verification, please disregard this email.\n\n" +
                        "If you experience any issues or have any questions, feel free to contact our support team at split.pay@gmail.com.\n\n" +
                        "Thank you for choosing Split Pay. We look forward to serving you!\n\n" +
                        "Best regards,\n" +
                        "The Split Pay Team\n";

        try {
            emailService.sendEmail(toEmail, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AccessTokenResponse createAccessTokenResponse(Users user) {
        List<String> scope = new ArrayList<>();
        ApplicationSetting applicationSettingByCode = applicationSettingsService.getApplicationSettingByCode(ApplicationSettingsCode.JWT_TOKEN_LIVE_MIN.getCode()).orElseThrow(
                ()->new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );

        int tokenLiveMinutes = Integer.parseInt(applicationSettingByCode.getSettingValue());

        final String jwt = JWTUtils.generateToken(user.getUserIdentity(), String.valueOf(tokenLiveMinutes), jwtSecretKey);
        final var coRelationId = UserContextHolder.getCorrelationIdFromContext() != null ? UserContextHolder.getCorrelationIdFromContext() : UUID.randomUUID().toString();

        final var accessTokenRedis = new AccessTokenResponse();
        accessTokenRedis.setAccessToken(jwt);
        accessTokenRedis.setCoRelationId(coRelationId);
        accessTokenRedis.setUsername(user.getUsername());
        accessTokenRedis.setUserIdentity(user.getUserIdentity());
        accessTokenRedis.setEmail(user.getEmail());
        accessTokenRedis.setPhoneNumber(user.getPhoneNumber());
        accessTokenRedis.setScope(scope);
        return accessTokenRedis;
    }



    @Override
    public TokenResponse authenticate(AuthenticationRequest request) {

        String username = request.getUserName();
        String password = request.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AuthenticationFailedException(ResponseMessage.UNMATCHED_USERNAME_OR_PASSWORD);
        }
        Users users = getUser(request);
        validatePassword(request, users);
        return users.getIsActive().equals(Boolean.TRUE)? handleActiveUser(request, users):handleInactiveUser(request, users);
    }

    public Boolean deleteTokenAndSession(String userIdentity) {
        var redisToken = redisTokenRepository.get(userIdentity);
        if (redisToken != null) {
            redisTokenRepository.delete(userIdentity);
            return true;
        }
        if (redisToken != null && redisToken.getAccessToken() != null) {
            redisSessionIdRepository.delete(redisToken.getAccessToken());
            return true;
        }
        return false;
    }

    private TokenResponse handleInactiveUser(AuthenticationRequest request, Users users) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setIsActive(users.getIsActive());
        return tokenResponse;
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
        String user = getCurrentUserContext().getUserName();
        List<Connection> pendingConnections = connectionRepository.findByConnectedUserAndConnectionStatus(user, ConnectionStatus.PENDING);
        return pendingConnections.stream()
                .map(Connection::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public void acceptRequest(ConnectionUpdate connectionUpdate) {
        String senderUserName = getCurrentUserContext().getUserName();
        Connection connection = connectionRepository.findByUserNameAndConnectedUserAndConnectionStatus(connectionUpdate.getConnectedUser(), senderUserName, ConnectionStatus.PENDING).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
        connection.setConnectionStatus(Objects.equals(connectionUpdate.getConnectionStatus(), ConnectionStatus.CONNECTED.toString()) ? ConnectionStatus.CONNECTED : ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
    }

    @Override
    public void unfriendUser(String receiverName) {
        String senderUserName = getCurrentUserContext().getUserName();
        Optional<Connection> directConnection = connectionRepository.findByUserNameAndConnectedUser(senderUserName, receiverName);
        Optional<Connection> byDirectionalConnection = connectionRepository.findByUserNameAndConnectedUser(receiverName, senderUserName);
        if (directConnection.isPresent()) {
            directConnection.get().setConnectionStatus(ConnectionStatus.UNFRIENDED);
            connectionRepository.save(directConnection.get());
        } else if (byDirectionalConnection.isPresent()) {
            byDirectionalConnection.get().setConnectionStatus(ConnectionStatus.UNFRIENDED);
            connectionRepository.save(byDirectionalConnection.get());
        } else {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

    }

    @Override
    public List<String> getUserFriends() {
        String user = getCurrentUserContext().getUserName();
        List<Connection> connections = connectionRepository.findByUserNameOrConnectedUserAndConnectionStatus(user, user, ConnectionStatus.CONNECTED);
        return connections.stream()
                .map(connection -> connection.getConnectedUser().equals(user) ? connection.getUserName() : connection.getConnectedUser())
                .collect(Collectors.toList());
    }

    @Override
    public void validateUser(ValidateUserEmail verificationCode) {

        Users user = userRepository.findByUsername(verificationCode.getUserName()).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
        if(user.getVerificationCode().equals(verificationCode.getVerificationCode())) {
            user.setIsActive(Boolean.TRUE);
            userRepository.save(user);
            return;
        }
        throw new UnauthorizedResourceException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS);
    }

    private void createConnectionRequest(String receiver) {
        String senderUserName = getCurrentUserContext().getUserName();
        Connection connection = createConnection(senderUserName, receiver);
        connectionRepository.save(connection);
    }

    private Connection createConnection(String senderUserName, String receiver) {
        Optional<Connection> connection = connectionRepository.findByUserNameAndConnectedUser(senderUserName, receiver);
        connection.ifPresent(connection1 -> connection1.setConnectionStatus(ConnectionStatus.PENDING));
        return connection.orElseGet(() -> Connection.builder()
                .userName(senderUserName)
                .connectedUser(receiver)
                .connectionStatus(ConnectionStatus.PENDING)
                .connectedAt(LocalDateTime.now())
                .build());
    }

    private void validateReceiverId(String receiverId) {
        if (StringUtils.isBlank(receiverId)) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        userRepository.findByUsername(receiverId).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
        String senderUserName = getCurrentUserContext().getUserName();
        connectionRepository.findByUserNameAndConnectedUserAndConnectionStatusIn(senderUserName, receiverId, List.of(ConnectionStatus.PENDING, ConnectionStatus.CONNECTED)).ifPresent(
                connection -> {
                    throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
                }
        );
        //bidirectional connection
        connectionRepository.findByUserNameAndConnectedUserAndConnectionStatusIn(receiverId, senderUserName, List.of(ConnectionStatus.PENDING, ConnectionStatus.CONNECTED)).ifPresent(
                connection -> {
                    throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
                }
        );
    }

    private void validateSelfConnection(String receiverId) {
        CurrentUserContext currentUserContext = getCurrentUserContext();
        if (currentUserContext.getUserName().equals(receiverId)) {
            throw new UnauthorizedResourceException(ResponseMessage.UNAUTHORIZED_CONNECTION_REQUEST);
        }
    }

    private TokenResponse handleActiveUser(AuthenticationRequest request, Users users) {
        final AccessTokenResponse response = this.createAccessTokenResponse(users);
        final String sessionId = SessionIdUtil.getSessionId();
        final String redisAccessTokenValue = ChecksumUtil.createChecksum(response.getAccessToken());
        redisService.deleteTokenAndSession(users.getUserIdentity());
        redisService.saveSession(redisAccessTokenValue, sessionId);
        UserJwtPayload userJwtPayload = authService.prepareAccessJwtPayload(users);
        String accessToken = authService.generateAccessToken(userJwtPayload);
        final RedisAccessToken redisAccessToken = new RedisAccessToken();
        BeanUtils.copyProperties(response, redisAccessToken);
        redisAccessToken.setAccessToken(redisAccessTokenValue);
        redisService.saveToken(redisAccessToken);
        return new TokenResponse(accessToken,users.getIsActive());
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
        users.setVerificationCode(generateVerificationCode());
        return users;
    }

    private void validateRequest(SignUpRequest request) {
        validateUserName(request.getUsername());
        validateUserEmail(request.getEmail());
    }

    private void validateUserEmail(String email) {
        Optional<Users> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new AlreadyExists(ResponseMessage.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateUserName(String username) {
        Optional<Users> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            throw new AlreadyExists(ResponseMessage.USER_NAME_ALREADY_EXISTS);
        }
    }

    private String generateVerificationCode() {
        return RandomGenerator.generateVerificationCode();
    }
}