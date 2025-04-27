package com.brainstation23.skeleton.presenter.domain.request.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
public class LinkRequest {
    @NotNull
    private String fromAccount;
    @NotNull
    private String transferType;
    @NotNull()
    @Positive(message = "Priority must be greater than zero")
    @Min(value = 1, message = "Priority must be greater than or equal to 1")
    private Long priority;

    @AssertTrue(message = "Priority must be greater than or equal to 1")
    public boolean isPriorityValid() {
        return priority != null && priority >= 1;
    }
}
