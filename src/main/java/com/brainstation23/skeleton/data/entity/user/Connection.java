package com.brainstation23.skeleton.data.entity.user;

import com.brainstation23.skeleton.core.domain.enums.ConnectionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "connections", schema = "user_service")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "connected_user", nullable = false)
    private String connectedUser;

    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_status", nullable = false)
    private ConnectionStatus connectionStatus;

}
