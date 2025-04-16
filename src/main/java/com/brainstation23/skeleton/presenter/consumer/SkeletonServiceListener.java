package com.brainstation23.skeleton.presenter.consumer;

import com.brainstation23.kafka.domain.EventWrapper;
import com.brainstation23.kafka.producer.exception.DuplicateEventException;
import com.brainstation23.kafka.producer.exception.Retryable;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.IIdempotentConsumerService;
import com.brainstation23.skeleton.core.service.impl.kafka.ProcessedEventRepositoryService;
import com.brainstation23.skeleton.presenter.domain.event.RequestEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Author: Md. Himon Shekh
 * Date: 11/21/2023 6:00 PM
 */

@Service
@RequiredArgsConstructor
public class SkeletonServiceListener extends BaseService {

    private final IIdempotentConsumerService iIdempotentConsumerService;
    private final ProcessedEventRepositoryService eventRepositoryService;

    @KafkaListener(topics = "${bs.kafka.topic.skeleton-test:default}", groupId = "${bs.kafka.consumer.group-id}")
    public void skeletonServiceTestListener(@Payload List<EventWrapper<RequestEvent>> events,
                                            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                                            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                                            @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        printTraceEventRequestData(events, keys, partitions, offsets);

        IntStream.range(0, events.size())
                .forEach(i -> {
                    EventWrapper<RequestEvent> eventWrapper = new ObjectMapper().convertValue(events.get(i), new TypeReference<>() {
                    });

                    try {

                        iIdempotentConsumerService.processRequest(eventWrapper);

                        eventRepositoryService.updateProcessedEventStatus(eventWrapper.getEventId());
                    } catch (DuplicateEventException e) {
                        logger.trace("Duplicate message received: " + e.getMessage());
                    } catch (Exception e) {

                        if (e instanceof Retryable) {
                            logger.trace("Throwing retryable exception.");
                            eventRepositoryService.saveRetriedProcessedEventStatus(eventWrapper.getEventId());
                            throw e;
                        }
                        logger.error("Error processing message: " + e.getMessage());

                        eventRepositoryService.updateIfExistOrInit(eventWrapper.getEventId());
                    }
                });
    }

    private void printTraceEventRequestData(final List<EventWrapper<RequestEvent>> events,
                                            final List<String> keys,
                                            final List<Integer> partitions,
                                            final List<Long> offsets) {
        logger.trace(String.format("%s number of payment responses received with keys: %s, partitions: %s and offsets: %s",
                events.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()));
    }
}
