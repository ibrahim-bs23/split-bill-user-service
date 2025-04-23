package com.brainstation23.skeleton.core.domain.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDetailsResponse {

    private Long expenseId;

    private Double amount;

    private String category;

    private String currency;
}
