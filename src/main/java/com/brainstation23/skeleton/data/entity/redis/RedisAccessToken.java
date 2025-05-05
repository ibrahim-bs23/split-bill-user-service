package com.brainstation23.skeleton.data.entity.redis;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RedisAccessToken implements Serializable {
    private String email;
    private String phoneNumber;
    private String countryCallingCode;
    private String accessToken;
    private String username;
    private String userIdentity;
    private String userGroupCode;
    private String productTypeCode;

    private String sessionId;
    private String coRelationId;
    private List<String> scope;
    private String branchCode;
}
