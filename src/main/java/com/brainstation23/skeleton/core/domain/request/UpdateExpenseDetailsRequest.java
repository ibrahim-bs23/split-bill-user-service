package com.brainstation23.skeleton.core.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateExpenseDetailsRequest {
  private Long expenseId;
  private double spentAmount;
  private String category;
}
