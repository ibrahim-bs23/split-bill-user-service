package com.brainstation23.skeleton.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group", schema = "user_service")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Group ID cannot be null")
    @Column(name = "group_id", nullable = false, unique = true)
    private String groupId;

    @NotNull(message = "Group name cannot be null")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, name = "CREATED_BY", columnDefinition = "varchar(100) default ''")
    private String createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "is_active")
    private boolean isActive = true;
}
