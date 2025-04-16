package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.common.logger.SkeletonServiceLogger;
import com.brainstation23.skeleton.common.utils.CorrelationContextHolder;
import com.brainstation23.skeleton.common.utils.SerializationUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.model.CurrentUserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class BaseService {

    protected SkeletonServiceLogger logger;
    protected LocaleMessageService messageService;
    protected HttpServletRequest request;
    protected ObjectMapper objectMapper;

    public static final String CURRENT_USER_CONTEXT_HEADER = "CurrentContext";

    @Autowired
    public void setLogger(SkeletonServiceLogger logger) {
        this.logger = logger;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Lazy
    @Autowired
    public void setMessageService(LocaleMessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getMessage(String key) {
        return messageService.getLocalMessage(key);
    }

    public String getMessage(ResponseMessage key, Object... objects) {
        return messageService.getLocalMessage(key, objects);
    }

    public String getMessage(String key, Object... objects) {
        return messageService.getLocalMessage(key, objects);
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public Optional<String> getHeaderValue(String headerName) {

        try {
            return Optional.ofNullable(request.getHeader(headerName));
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return Optional.empty();
    }

    public CurrentUserContext getCurrentUserContext() {
        String base64Data = getCurrentUserContextHeaderValue();
        String jsonObject = SerializationUtils.toByteArrayToString(base64Data);
        return toObject(jsonObject, CurrentUserContext.class);
    }

    public CurrentUserContext getCurrentUserContext(String token) {
        String jsonObject = SerializationUtils.toByteArrayToString(token);
        return toObject(jsonObject, CurrentUserContext.class);
    }

    public <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public String getCurrentUserContextHeaderValue() {
        Optional<String> userTokenOpt = getHeaderValue(CURRENT_USER_CONTEXT_HEADER);

        if (userTokenOpt.isEmpty())
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());

        return userTokenOpt.get();
    }

    public String getUserIdentity() {
        return getCurrentUserContext().getUserIdentity();
    }

    public String getCorrelationId() {
        return CorrelationContextHolder.getCorrelationIdFromContext();
    }

//    public EventWrapper<Object> prepareKafkaObject(String requestId, Timestamp timestamp, Object data) {
//        return new EventWrapper<>(requestId, timestamp, data);
//    }

    public <T> void printTrace(T obj) {
        logger.trace(writeJsonString(obj));
    }

    public <T> String writeJsonString(T obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return StringUtils.EMPTY;
    }

    public static long getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
