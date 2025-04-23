package com.brainstation23.skeleton.data.entity.expense;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "event_expense_invoice",schema = "user_service")
@Entity
public class EventExpenseInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long individualExpenseId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Date createdAt;

}
