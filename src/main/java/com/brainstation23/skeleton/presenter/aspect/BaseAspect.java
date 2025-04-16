package com.brainstation23.skeleton.presenter.aspect;

import com.brainstation23.skeleton.common.utils.SerializationUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.model.CurrentUserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BaseAspect {
    public final Logger traceLogger = LoggerFactory.getLogger("traceLogger");
    public final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    public HttpServletRequest request;
    private ObjectMapper objectMapper;

    public static final String CURRENT_USER_CONTEXT_HEADER = "CurrentContext";

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<String> getHeaderValue(String headerName) {

        try {
            return Optional.ofNullable(request.getHeader(headerName));
        } catch (Exception ex) {
            errorLogger.error(ex.getLocalizedMessage(), ex);
        }

        return Optional.empty();
    }

    public CurrentUserContext getCurrentUserContext() {
        String base64Data = getCurrentUserContextHeaderValue();
        String jsonObject = SerializationUtils.toByteArrayToString(base64Data);
        return toObject(jsonObject, CurrentUserContext.class);
    }

    public String getCurrentUserContextHeaderValue() {
        Optional<String> userTokenOpt = getHeaderValue(CURRENT_USER_CONTEXT_HEADER);

        if (userTokenOpt.isEmpty())
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage());

        return userTokenOpt.get();
    }

    public <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            errorLogger.error(e.getMessage());
        }
        return null;
    }

}
