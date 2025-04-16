package com.brainstation23.skeleton.data.repository.kafka;


import com.brainstation23.skeleton.data.entity.kafka.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    Optional<Outbox> findByEventId(String eventId);
}
