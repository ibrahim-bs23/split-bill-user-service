package com.brainstation23.skeleton.data.repository.redis;

import com.brainstation23.skeleton.data.entity.redis.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {
    Optional<Message> findByKeyAndLocale(String key, String locale);
}
