package com.brainstation23.skeleton.data.entity.kafka;

import com.brainstation23.kafka.enums.OutboxStatus;
import com.brainstation23.skeleton.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "OUTBOX")
public class Outbox extends BaseEntity {

    @Column(name = "TOPIC")
    private String topic;

    @Column(name = "KEY")
    private String key;

    @Column(name = "EVENT_ID")
    private String eventId;

    @Column(name = "CORRELATION_ID")
    private String correlationId;

    @Column(name = "PAYLOAD", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

}