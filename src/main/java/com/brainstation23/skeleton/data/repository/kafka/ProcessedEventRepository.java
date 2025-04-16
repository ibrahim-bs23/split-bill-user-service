package com.brainstation23.skeleton.data.repository.kafka;


import com.brainstation23.skeleton.data.entity.kafka.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {

    Optional<ProcessedEvent> findByEventId(String eventId);

}
