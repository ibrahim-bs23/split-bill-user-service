package com.brainstation23.skeleton.core.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class AccessTokenResponse implements Serializable {
    private String accessToken;

    private String email;
    private String username;
    private String phoneNumber;
    private String countryCallingCode;
    private String userIdentity;
    private String userGroupCode;
    private String productTypeCode;

    private String sessionId;
    private String coRelationId;
    private String passwordExpiryDate;
    private boolean isDeviceBindingNeeded;
    private boolean isCredentialChangeNeeded;
    private boolean isUsernamePasswordChangeNeeded;
    private List<String> scope;
    private String branchCode;
}
