package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.data.entity.redis.RedisAccessToken;
import com.brainstation23.skeleton.data.entity.redis.RedisSessionId;
import com.brainstation23.skeleton.data.repository.redis.RedisSessionIdRepository;
import com.brainstation23.skeleton.data.repository.redis.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    final RedisTokenRepository redisTokenRepository;
    final RedisSessionIdRepository redisSessionIdRepository;

    final RedisTemplate<String, String> redisTemplate;
    final ApplicationSettingsService applicationConfigService;

    public void delete(String key) {
        redisTemplate.delete(key);
    }


    public void saveToken(RedisAccessToken redisAccessToken) {
        redisTokenRepository.save(redisAccessToken);
    }

    public RedisAccessToken getToken(String userIdentity) {
        return redisTokenRepository.get(userIdentity);
    }

    public void saveSession(String key, String session) {
        final RedisSessionId redisSessionId = RedisSessionId.builder().sessionRedisKey(key).sessionId(session).build();
        redisSessionIdRepository.save(redisSessionId);
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

}
