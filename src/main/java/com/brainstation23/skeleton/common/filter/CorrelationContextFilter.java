package com.brainstation23.skeleton.common.filter;

import com.brainstation23.skeleton.common.utils.CorrelationContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Author: Md. Himon Shekh
 * Date: 12/12/2023 6:21 PM
 */
@Component
public class CorrelationContextFilter implements Filter {
    private static String CORRELATION_ID = "correlationId";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws ServletException, IOException {
        final String correlationId = ((HttpServletRequest) request).getHeader(CORRELATION_ID);

        CorrelationContextHolder.setCorrelationIdInContext(correlationId);

        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(CORRELATION_ID, correlationId);

        chain.doFilter(request, response);
    }
}
