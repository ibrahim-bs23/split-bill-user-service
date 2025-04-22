package com.brainstation23.skeleton.core.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDetailsRequest {
  private String eventId;
  private double spentAmount;
  private String category;
}
