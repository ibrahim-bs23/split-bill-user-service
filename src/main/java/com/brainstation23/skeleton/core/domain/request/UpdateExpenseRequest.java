package com.brainstation23.skeleton.core.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateExpenseRequest {
   private Long individualExpenseId;
   private Double spentAmount;
}
