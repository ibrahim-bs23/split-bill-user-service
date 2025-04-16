package com.brainstation23.skeleton.common.handlers;

import com.brainstation23.skeleton.common.logger.SkeletonServiceLogger;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ApiResponseCode;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.*;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.LocaleMessageService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class SkeletonServiceExceptionHandler extends BaseExceptionHandler {

    private final LocaleMessageService localeMessageService;
    private final SkeletonServiceLogger logger;

    @ExceptionHandler({
            ServiceDomainException.class,
            DatabaseException.class,
            InvalidRequestDataException.class,
            OperationFailedException.class,
            OperationHoldException.class,
            TransactionReverseException.class,
            UnauthorizedResourceException.class,
            RecordNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomRootException ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ex.getMessageCode(), getMessage(ex.getMessage()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler({InterServiceCommunicationException.class})
    public ResponseEntity<ApiResponse<Void>> handleFeignClientException(CustomRootException ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<ApiResponse<Void>> handleFeignException(FeignException ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(), getMessage(ResponseMessage.INTER_SERVICE_COMMUNICATION_ERROR.getResponseMessage()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Void>> commonException(Exception ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.UNHANDLED_EXCEPTION.getResponseCode(), getMessage(ResponseMessage.INTERNAL_SERVICE_EXCEPTION.getResponseMessage()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(FeignClientException.class)
    public final ResponseEntity<ApiResponse<Void>> handleFeignClientException(FeignClientException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> collect = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            collect.put(fieldError.getField(), getMessage(fieldError.getDefaultMessage()));
        }

        String message = getMessage(ResponseMessage.INVALID_REQUEST_DATA.getResponseMessage());
        ApiResponse<Object> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), message, collect);

        dropErrorLogForArgumentNotValid("****Custom Jakarta Validation Error**** ", ex.getParameter().getDeclaringClass().getName(),
                Objects.isNull(ex.getParameter().getMethod()) ? "" : ex.getParameter().getMethod().getName(),
                message,
                collect);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    private void dropErrorLogForArgumentNotValid(final String logHeader, final String className, final String methodName, final String message, final Object data) {

        errorLogger.error(String.format(logHeader +
                "\nClassName: %s | MethodName: %s | Message : %s" +
                "\nError Data: %s", className, methodName, message, data));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = getMessage(ResponseMessage.INVALID_REQUEST_METHOD_TYPE.getResponseMessage());
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Object> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.METHOD_NOT_ALLOWED.getResponseCode(), message);

        return new ResponseEntity<>(apiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private String getMessage(String messageKey) {
        String message = StringUtils.EMPTY;

        try {
            message = localeMessageService.getLocalMessage(messageKey);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return StringUtils.isNotBlank(message) ? message : messageKey;
    }

}
