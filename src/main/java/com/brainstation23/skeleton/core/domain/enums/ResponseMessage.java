package com.brainstation23.skeleton.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    OPERATION_SUCCESSFUL("operation.success"),
    RECORD_NOT_FOUND("record.not.found"),
    LOCALE_RECORD_NOT_FOUND("locale.record.not.found"),
    INTER_SERVICE_COMMUNICATION_ERROR("inter.service.communication.exception"),
    INTERNAL_SERVICE_EXCEPTION("internal.service.exception"),
    DATABASE_EXCEPTION("database.exception"),
    TEMPLATE_PARAM_COUNT_MISMATCH("template.param.count.mismatch"),
    TEMPLATE_PARAM_MISMATCH("template.param.mismatch"),
    TEMPLATE_PROCESSING_ERROR("template.processing.error"),
    INVALID_REQUEST_DATA("invalid.request.data"),
    INVALID_REQUEST_METHOD_TYPE("invalid.request.method.type"),
    SMS_DATA_EXCEPTION("invalid.sms.data"),
    TEMPLATE_ACTIVENESS_ERROR("invalid.request.data"),
    TEMPLATE_PARAM_TYPO("template.param.typo"),
    JSON_PARSE_ERROR("json.parse.error"),
    RECORD_ALREADY_EXIST("record.already.exist"),
    UNAUTHORIZED_RESOURCE_ACCESS("unauthorized.resource.access"),
    EVENT_PUBLISH_ERROR("event.publish.error"),
    ;
    private final String responseMessage;
}
