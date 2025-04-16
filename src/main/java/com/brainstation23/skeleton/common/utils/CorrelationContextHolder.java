package com.brainstation23.skeleton.common.utils;

import org.slf4j.MDC;

/**
 * Author: Md. Himon Shekh
 * Date: 12/2/2023 12:46 AM
 */
public final class CorrelationContextHolder {
    private static final String CORRELATION_ID = "correlationId";

    private CorrelationContextHolder() {
    }

    public static void setCorrelationIdInContext(final String value) {
        MDC.put(CORRELATION_ID, value);
    }

    public static String getCorrelationIdFromContext() {
        return MDC.get(CORRELATION_ID);
    }

    public static void clear() {
        MDC.clear();
    }
}
