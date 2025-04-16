package com.brainstation23.skeleton.core.service.impl.kafka.consumer;

import com.brainstation23.kafka.domain.EventWrapper;
import com.brainstation23.kafka.producer.exception.DuplicateEventException;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.IIdempotentConsumerService;
import com.brainstation23.skeleton.data.entity.kafka.ProcessedEvent;
import com.brainstation23.skeleton.data.repository.kafka.ProcessedEventRepository;
import com.brainstation23.skeleton.presenter.domain.event.RequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotentConsumerService extends BaseService implements IIdempotentConsumerService {
    private final ProcessedEventRepository processedEventRepository;

    @Override
    public void processRequest(EventWrapper<RequestEvent> event) {
        deduplicate(event.getEventId());
        logger.trace(writeJsonString(event));

    }

    private void deduplicate(final String eventId) throws DuplicateEventException {
        try {
            processedEventRepository.saveAndFlush(new ProcessedEvent(eventId));
            logger.trace("Event persisted with Id: {}" + eventId);
        } catch (DataIntegrityViolationException e) {
            logger.trace("Event already processed: {}" + eventId);
            throw new DuplicateEventException(eventId);
        }
    }
}
