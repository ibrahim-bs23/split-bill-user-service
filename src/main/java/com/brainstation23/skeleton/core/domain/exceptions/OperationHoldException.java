package com.brainstation23.skeleton.core.domain.exceptions;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;

public class OperationHoldException extends CustomRootException {
    private static final String MESSAGE_CODE = "EHLD401";

    public OperationHoldException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public OperationHoldException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
