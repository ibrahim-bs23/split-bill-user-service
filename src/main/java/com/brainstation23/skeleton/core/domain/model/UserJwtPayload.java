package com.brainstation23.skeleton.core.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Getter
@Jacksonized
@Builder(toBuilder = true)
public class UserJwtPayload implements Serializable {
    private String userName;
    private String sessionId;
}
