package com.brainstation23.skeleton.presenter.domain.request.user;

import com.brainstation23.skeleton.core.domain.enums.ConnectionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ConnectionUpdate {
    @NotNull
    String connectedUser;

    @NotNull
    String connectionStatus;
}
