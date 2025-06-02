package com.brainstation23.skeleton.core.domain.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualExpenseResponse {

    private Long individualExpenseId;

    private String userName;

    private String userIdentity;

    private Double spentAmount;

    private String currency;

    private Double dueAmount;

    private Double outstandingBalance;

    private Double budgetAmount;

    private String paymentStatus;

    private Boolean isEditable;
}
