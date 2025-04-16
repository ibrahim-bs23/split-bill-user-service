package com.brainstation23.skeleton.presenter.consumer.common;

import com.brainstation23.kafka.domain.EventWrapper;
import com.brainstation23.skeleton.core.service.impl.kafka.OutboxRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Author :  Md. Himon shekh
 * Created at : 14 Dec 2023
 */
@Service
@RequiredArgsConstructor
public class DefaultEventListener {
    private final OutboxRepositoryService outboxRepositoryService;

    @KafkaListener(topics = "#{T(com.brainstation23.skeleton.common.utils.KafkaTopics).getTopics()}", groupId = "skeleton_outbox")
    public void testListener(@Payload EventWrapper<?> event) {

        final String eventId = event.getEventId();

        outboxRepositoryService.updateOutboxStatus(eventId);
    }
}
