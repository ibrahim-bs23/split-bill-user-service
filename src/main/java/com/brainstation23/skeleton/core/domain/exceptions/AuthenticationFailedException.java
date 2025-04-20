package com.brainstation23.skeleton.core.domain.exceptions;


import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;

public class AuthenticationFailedException extends CustomRootException {
    private static final String MESSAGE_CODE = "EAD401";

    public AuthenticationFailedException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public AuthenticationFailedException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
