package com.brainstation23.skeleton.core.domain.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class GroupResponse {
    private String groupId;

    private String name;

    private String description;

    private String createdBy;

    private Date createdAt;

    private boolean isActive;
}
