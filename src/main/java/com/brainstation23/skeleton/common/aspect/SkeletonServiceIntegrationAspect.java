package com.brainstation23.skeleton.common.aspect;

import com.brainstation23.skeleton.common.logger.CommonIntegrationLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SkeletonServiceIntegrationAspect extends CommonIntegrationLoggerAspect {

    @Pointcut("execution(public * com.brainstation23.skeleton.presenter.service..*.*(..))")
    public void skeletonIntegrationTrace() {
    }

    @Around("skeletonIntegrationTrace()")
    public Object traceNotificationIntegration(ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
