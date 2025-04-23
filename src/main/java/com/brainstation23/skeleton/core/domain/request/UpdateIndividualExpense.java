package com.brainstation23.skeleton.core.domain.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIndividualExpense {
    private String eventId;
}
