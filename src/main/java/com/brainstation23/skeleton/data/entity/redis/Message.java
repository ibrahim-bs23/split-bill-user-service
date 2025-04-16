package com.brainstation23.skeleton.data.entity.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Setter
@RedisHash("message")
public class Message implements Serializable {
    @Id
    private String id;
    @Indexed
    private String locale;
    @Indexed
    private String key;
    private String content;
}
