package com.brainstation23.skeleton.core.domain.request;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupMemberRequest {

    private UUID userIdentity;
    private String role;

}
