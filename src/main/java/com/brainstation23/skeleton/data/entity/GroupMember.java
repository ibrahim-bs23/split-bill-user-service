package com.brainstation23.skeleton.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_member", schema = "user_service")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Group cannot be null")
    private String groupId;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_identity", nullable = false)
    private String userIdentity;

    @Column(name = "role", length = 50)
    private String role;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "CREATED_AT")
    private Date createdAt;

}
