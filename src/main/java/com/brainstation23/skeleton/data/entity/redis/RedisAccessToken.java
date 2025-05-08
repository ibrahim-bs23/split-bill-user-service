package com.brainstation23.skeleton.data.entity.redis;

import com.brainstation23.skeleton.core.domain.enums.UserTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.usertype.UserType;

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
    private UserTypeEnum userType;

    private String sessionId;
    private String coRelationId;
    private List<String> scope;
    private String branchCode;
}
