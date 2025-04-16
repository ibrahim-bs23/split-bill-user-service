package com.brainstation23.skeleton.core.domain.exceptions;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;

public class ServiceDomainException extends CustomRootException {
    private static final String MESSAGE_CODE = "ERPS412";

    public ServiceDomainException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public ServiceDomainException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
