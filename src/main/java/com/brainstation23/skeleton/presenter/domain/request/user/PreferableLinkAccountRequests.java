package com.brainstation23.skeleton.presenter.domain.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PreferableLinkAccountRequests {
    @NotNull
    private String senderUsername;
    @NotNull
    private String receiverUsername;
}
