package com.brainstation23.skeleton.presenter.domain.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username can't exceed 100 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email can't exceed 255 characters")
    private String email;

    @Size(max = 20, message = "Phone number can't exceed 20 characters")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20)
    private String password;

    private String profilePhoto;

}
