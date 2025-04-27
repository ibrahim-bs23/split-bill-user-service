package com.brainstation23.skeleton.presenter.domain.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateLinkRequest {

    @NotNull()
    private Long id;
    @NotNull()
    @Positive()
    @Min(value = 1)
    private Long priority;
}
