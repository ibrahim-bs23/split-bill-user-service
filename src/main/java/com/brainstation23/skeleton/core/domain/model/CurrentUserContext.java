package com.brainstation23.skeleton.core.domain.model;

import com.brainstation23.skeleton.core.domain.enums.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CurrentUserContext implements Serializable {
    private String userIdentity;
    private String userName;
    private UserTypeEnum userType;
    private Integer userLevel;
    private Integer userStatus;
    private String userPhoneNo;
    private String userCbsClientId;
    private String groupId;
    private String userEmail;
    private List<String> scopes;
}
