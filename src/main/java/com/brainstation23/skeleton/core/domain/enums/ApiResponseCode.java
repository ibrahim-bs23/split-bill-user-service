package com.brainstation23.skeleton.core.domain.enums;

import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {

    OPERATION_SUCCESSFUL("S100000"),
    RECORD_NOT_FOUND("SK404"),
    INVALID_REQUEST_DATA("SK400"),
    INTER_SERVICE_COMMUNICATION_ERROR("SK503"),
    DB_OPERATION_FAILED("SK422"),
    UNHANDLED_EXCEPTION("SK500"),
    METHOD_NOT_ALLOWED("SK405"),
    UNAUTHORIZED_RESOURCE_ACCESS("SK401"),
    ;

    private final String responseCode;

    public static boolean isOperationSuccessful(ApiResponse apiResponse) {
        return Objects.nonNull(apiResponse) && apiResponse.getResponseCode().equals(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
    }

    public static boolean isNotOperationSuccessful(ApiResponse apiResponse) {
        return !isOperationSuccessful(apiResponse);
    }

}
