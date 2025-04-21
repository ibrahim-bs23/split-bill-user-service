package com.brainstation23.skeleton.common.mapper;

import com.brainstation23.skeleton.data.entity.user.Users;
import com.brainstation23.skeleton.presenter.domain.request.user.SignUpRequest;
import com.brainstation23.skeleton.presenter.domain.response.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public Users toUser(SignUpRequest request) {
        String profilePhoto = request.getProfilePhoto() != null ? request.getProfilePhoto() : "default-profile-photo.jpg";
        return Users.builder()
                .username(request.getUsername())
                .userIdentity(UUID.randomUUID().toString().replace("-", ""))
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .profilePhoto(profilePhoto)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public UserResponseDTO toUserResponseDTO(Users users) {
        return UserResponseDTO.builder()
                .id(users.getId())
                .userIdentity(users.getUserIdentity())
                .username(users.getUsername())
                .email(users.getEmail())
                .phoneNumber(users.getPhoneNumber())
                .profilePhoto(users.getProfilePhoto())
                .build();
    }
}
