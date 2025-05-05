package com.brainstation23.skeleton.data.repository.redis;

import com.brainstation23.skeleton.data.entity.redis.RedisSessionId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSessionIdRepository {
    private static final String FOLDER = "session:jwt-";

    private final RedisTemplate<String, String> template;

    public void save(RedisSessionId entity) {
        entity.setTtlTimeInMinutes(5L);
        template.opsForValue().set(FOLDER.concat(entity.getSessionRedisKey()), entity.getSessionId(), entity.getTtlTimeInMinutes(), TimeUnit.MINUTES);
    }

    public String get(String key) {
        return template.opsForValue().get(FOLDER.concat(key));
    }

    public void delete(String key) {
        template.delete(FOLDER.concat(key));
    }

}
