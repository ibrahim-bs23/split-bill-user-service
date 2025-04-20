package com.brainstation23.skeleton.core.jwt.service;


import com.brainstation23.skeleton.common.utils.CryptoUtils;
import com.brainstation23.skeleton.common.utils.JWTUtils;
import com.brainstation23.skeleton.core.domain.model.UserJwtPayload;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.data.entity.user.Users;
import com.brainstation23.skeleton.data.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends BaseService {

    private final UserRepository userRepository;

    private static final String CORRELATION_ID = "correlationId";

    @Value("${jwt.token.prefix}")
    protected String tokenPrefix;

    @Value("${jwt.secret.key}")
    protected String jwtSecretKey;

    @Value("${jwt.id-token.secret.key}")
    protected String jwtIdTokenSecretKey;

    @Value("${payload.encryption.secret.key}")
    protected String encryptionSecretKey;

    @Value("${jwt.token.expiry.minute}")
    protected String jwtExpiryTime;


    public String generateAccessToken(UserJwtPayload payload) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> customerData = objectMapper.convertValue(payload, Map.class);
        claims.putAll(customerData);
        return JWTUtils.generateToken(claims, payload.getUserName(), jwtExpiryTime, jwtSecretKey);
    }

    public UserJwtPayload prepareAccessJwtPayload(Users user) {
        return UserJwtPayload
                .builder()
                .userName(CryptoUtils.encrypt(user.getUsername(), encryptionSecretKey))
                .sessionId(getCorrelationIdFromRequest())
                .build();
    }

    public String getCorrelationIdFromRequest() {
        return getHeaderValue(CORRELATION_ID)
                .orElse(generateCorrelationId());
    }
    public String generateCorrelationId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
