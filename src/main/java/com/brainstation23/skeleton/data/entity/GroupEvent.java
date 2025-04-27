package com.brainstation23.skeleton.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_event", schema = "user_service")
public class GroupEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "user_identity", nullable = false)
    private String userIdentity;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "event_date", nullable = false)
    private Date eventDate;

    @Column(name = "total_spending")
    private BigDecimal totalSpending;

    @Column(name = "event_status", nullable = false)
    private String eventStatus = "ACTIVE";

}
