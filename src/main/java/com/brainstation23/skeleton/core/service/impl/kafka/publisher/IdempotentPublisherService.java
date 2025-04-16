package com.brainstation23.skeleton.core.service.impl.kafka.publisher;

import com.brainstation23.kafka.domain.EventWrapper;
import com.brainstation23.kafka.enums.OutboxStatus;
import com.brainstation23.skeleton.common.utils.CorrelationContextHolder;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.IIdempotentPublisherService;
import com.brainstation23.skeleton.data.entity.kafka.Outbox;
import com.brainstation23.skeleton.data.repository.kafka.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotentPublisherService extends BaseService implements IIdempotentPublisherService {
    private final OutboxRepository outboxRepository;

    @Override
    public void processOutboxEvent(final Object event, final String topic) {
        final EventWrapper<Object> requestEvent = new EventWrapper<>();
        requestEvent.setEventId(getRandomUUID());
        requestEvent.setCorrelationId(CorrelationContextHolder.getCorrelationIdFromContext());
        requestEvent.setTimestamp(getCurrentTimestamp());
        requestEvent.setData(event);

        writeOutboxEventForPaymentAcknowledgement(requestEvent, topic);
    }

    private void writeOutboxEventForPaymentAcknowledgement(final EventWrapper<Object> event, final String topic) {

        final Outbox outbox = new Outbox();

        try {
            outbox.setTopic(topic);
            outbox.setKey(StringUtils.EMPTY);
            outbox.setPayload(objectMapper.writeValueAsString(event));
            outbox.setEventId(event.getEventId());
            outbox.setCorrelationId(event.getCorrelationId());
            outbox.setStatus(OutboxStatus.STARTED);
            outbox.setCreatedBy("RP internal");

            Long outboxEventId = outboxRepository.save(outbox).getId();

            logger.trace("Event persisted to transactional outbox with Id: {}" + outboxEventId);

        } catch (Exception ex) {
            logger.error("Failed to publish event during return payment acknowledgement! correlationId: " + event.getCorrelationId());
            logger.error(ex.getMessage(), ex);
            throw new InvalidRequestDataException(ResponseMessage.EVENT_PUBLISH_ERROR);
        }
    }
}
