package com.brainstation23.skeleton.data.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expense_split", schema = "user_service")
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "sender_username", nullable = false, length = 100)
    private String senderUserName;

    @Column(name = "receiver_username", nullable = false, length = 100)
    private String receiverUserName;

    @Column(name = "sender_user_identity", nullable = false)
    private String senderUserIdentity;

    @Column(name = "receiver_user_identity", nullable = false)
    private String receiverUserIdentity;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Column(name = "currency")
    private String currency;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "CREATED_AT")
    private Date createdAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "UPDATED_AT")
    private Date updatedAt;

}
