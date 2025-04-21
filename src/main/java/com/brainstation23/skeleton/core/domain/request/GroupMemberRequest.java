package com.brainstation23.skeleton.core.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupMemberRequest {

    private List<String> usernames;
    private String groupId;

}
