package com.brainstation23.skeleton.data.entity.user;

import com.brainstation23.skeleton.core.domain.enums.TransferType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "user_link_accounts", schema = "user_service")
public class UserLinkAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_identity", nullable = false)
    private String userIdentity;

    @Column(name = "from_account", nullable = false)
    private String fromAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false)
    private TransferType transferType;

    @Column(name = "priority", nullable = false)
    private Long priority;

}
