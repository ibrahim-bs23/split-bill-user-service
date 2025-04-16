package com.brainstation23.skeleton.core.service.impl.kafka;

import com.brainstation23.kafka.enums.OutboxStatus;
import com.brainstation23.skeleton.data.repository.kafka.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author :  Ahmmed Jubayer Rumman
 * Created at : 26 Nov 2023
 */
@Service
@RequiredArgsConstructor
public class OutboxRepositoryService {

    private final OutboxRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOutboxStatus(final String eventId) {
        repository.findByEventId(eventId)
                .ifPresent(outbox -> {
                    outbox.setStatus(OutboxStatus.COMPLETED);
                    repository.save(outbox);
                });
    }

}
