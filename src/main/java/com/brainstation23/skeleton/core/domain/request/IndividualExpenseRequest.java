package com.brainstation23.skeleton.core.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndividualExpenseRequest {
    private String eventId;
    private String groupId;
    private String userName;
    private String userIdentity;
}
