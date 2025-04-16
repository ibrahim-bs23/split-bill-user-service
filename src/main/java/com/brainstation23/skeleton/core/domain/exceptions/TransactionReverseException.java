package com.brainstation23.skeleton.core.domain.exceptions;


import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;

public class TransactionReverseException extends CustomRootException {
    private static final String MESSAGE_CODE = "EREV101";
    public TransactionReverseException(ResponseMessage message) {
        super(MESSAGE_CODE, message.getResponseMessage());
    }

    public TransactionReverseException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}
