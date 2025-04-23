package com.brainstation23.skeleton.core.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDetailsRequest {
  private String eventId;

  @NotNull(message = "Spent amount cannot be null")
  @Positive(message = "Spent amount must be positive")
  @Min(value = 0, message = "Spent amount cannot be less than zero")
  private double spentAmount;

  @NotNull(message = "Category cannot be null")
  private String category;
}
