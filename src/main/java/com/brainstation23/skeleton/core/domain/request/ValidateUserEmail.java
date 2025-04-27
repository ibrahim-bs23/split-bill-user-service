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
public class ValidateUserEmail {

  @NotNull(message = "User name cannot be null")
  private String userName;
  @NotNull
  private String verificationCode;
}
