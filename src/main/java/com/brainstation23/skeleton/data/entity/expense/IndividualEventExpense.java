package com.brainstation23.skeleton.data.entity.expense;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "individual_event_expense", schema = "user_service")
@Entity
public class IndividualEventExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String groupId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userIdentity;

    @Column(nullable = false)
    private Double spentAmount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Double dueAmount;

    @Column(nullable = false)
    private Double outstandingBalance;

    @Column(nullable = false)
    private Double budgetAmount;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isEditable = true;

    @Column(nullable = false)
    private String paymentStatus;

}
