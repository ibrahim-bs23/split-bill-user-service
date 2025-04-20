package com.brainstation23.skeleton.presenter.domain.request.auth;

import com.brainstation23.skeleton.presenter.domain.request.devicebinding.DeviceInfoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank
    private String userName;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

}
