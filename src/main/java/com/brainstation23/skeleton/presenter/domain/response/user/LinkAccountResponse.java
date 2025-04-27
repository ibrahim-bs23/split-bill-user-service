package com.brainstation23.skeleton.presenter.domain.response.user;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkAccountResponse {

    private Long id;
    private String fromAccount;
    private String transferType;
    private Long priority;
}
