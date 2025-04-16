package com.brainstation23.skeleton.presenter.aspect;

import com.brainstation23.skeleton.presenter.context.DatabaseContextHolder;
import com.brainstation23.skeleton.presenter.context.DatabaseEnvironment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Order(1)
public class TransactionReadonlyAspect {

    private static final Logger traceLogger = LoggerFactory.getLogger("traceLogger");

    @Around("@annotation(transactional)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, Transactional transactional) throws Throwable {
        traceLogger.trace("Database route trace executed");
        try {
            if (transactional.readOnly()) {
                DatabaseContextHolder.set(DatabaseEnvironment.READONLY);
            }
            return proceedingJoinPoint.proceed();
        } finally {
            DatabaseContextHolder.reset();
        }
    }
}
