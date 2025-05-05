package com.brainstation23.skeleton.presenter.context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Author: Md. Himon Shekh
 * Date: 12/2/2023 12:46 AM
 */
public final class UserContextHolder {
    private static final String CORRELATION_ID = "correlationId";
    private static final String TRACE_ID = "traceId";
    private static final String CURRENT_CONTEXT = "CurrentContext";

    private UserContextHolder() {
    }

    public static void setCorrelationIdInContext(final String value) {
        MDC.put(CORRELATION_ID, StringUtils.isBlank(value) ? getRandomUUID() : value);
    }

    public static String getCorrelationIdFromContext() {
        return MDC.get(CORRELATION_ID);
    }

    public static void setTraceIdInContext() {
        MDC.put(TRACE_ID, getRandomUUID());
    }

    public static String getTraceIdFromContext() {
        return MDC.get(TRACE_ID);
    }

    public static void setCurrentContext(final String value) {
        MDC.put(CURRENT_CONTEXT, value);
    }
    public static String getCurrentContext() {
        return MDC.get(CURRENT_CONTEXT);
    }

    public static void clear() {
        MDC.clear();
    }


    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
