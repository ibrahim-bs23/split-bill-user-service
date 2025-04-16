package com.brainstation23.skeleton.core.service.impl.kafka;

import com.brainstation23.kafka.enums.ProcessedEventStatus;
import com.brainstation23.skeleton.data.entity.kafka.ProcessedEvent;
import com.brainstation23.skeleton.data.repository.kafka.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessedEventRepositoryService {
    private final ProcessedEventRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProcessedEventStatus(String eventId) {
        repository.findByEventId(eventId)
                .ifPresent(processedEvent -> {
                    processedEvent.setStatus(ProcessedEventStatus.COMPLETED);
                    repository.save(processedEvent);
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedProcessedEventStatus(String eventId) {
        ProcessedEvent processedEvent = new ProcessedEvent(eventId);
        processedEvent.setStatus(ProcessedEventStatus.FAILED);
        repository.saveAndFlush(processedEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateIfExistOrInit(final String eventId) {
        final ProcessedEvent processedEvent = repository.findByEventId(eventId)
                .orElseGet(ProcessedEvent::new);

        processedEvent.setStatus(ProcessedEventStatus.FAILED);
        repository.saveAndFlush(processedEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRetriedProcessedEventStatus(String eventId) {
        ProcessedEvent processedEvent = new ProcessedEvent(UUID.randomUUID().toString());
        processedEvent.setRetriedEventId(eventId);
        processedEvent.setStatus(ProcessedEventStatus.FAILED);
        repository.saveAndFlush(processedEvent);
    }

}
