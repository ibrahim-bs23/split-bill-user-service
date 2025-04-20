package com.brainstation23.skeleton.core.domain.exceptions;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;

public class AlreadyExists extends CustomRootException {
    private static final String MESSAGE_CODE = "ERPS400";

    public AlreadyExists(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public AlreadyExists(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
