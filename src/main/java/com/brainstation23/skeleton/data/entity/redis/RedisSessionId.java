package com.brainstation23.skeleton.data.entity.redis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedisSessionId {
    private String sessionRedisKey;
    private String sessionId;
    private Long ttlTimeInMinutes;
}
