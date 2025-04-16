package com.brainstation23.skeleton.presenter.aspect;

import com.brainstation23.kafka.domain.EventWrapper;
import com.brainstation23.skeleton.common.utils.CorrelationContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-9)
public class CorrelationIdFilterAspect {

    @Pointcut("execution(* com.brainstation23.skeleton.core.service.impl.kafka.consumer..*.*(..)))")
    public void skeletonConsumerAspect() {
    }

    @Around("skeletonConsumerAspect()")
    public Object correlationIdFilter(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();

        if (args.length > 0 && args[0] instanceof EventWrapper<?> eventWrapper)
            CorrelationContextHolder.setCorrelationIdInContext(eventWrapper.getCorrelationId());

        try {
            return joinPoint.proceed(args);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
