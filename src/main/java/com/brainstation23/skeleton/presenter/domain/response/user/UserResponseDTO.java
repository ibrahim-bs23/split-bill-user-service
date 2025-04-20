package com.brainstation23.skeleton.presenter.domain.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private UUID userIdentity;
    private String username;
    private String email;
    private String phoneNumber;
    private String profilePhoto;

}
