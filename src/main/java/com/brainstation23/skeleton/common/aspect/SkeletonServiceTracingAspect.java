package com.brainstation23.skeleton.common.aspect;

import com.brainstation23.skeleton.common.logger.CommonTraceLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SkeletonServiceTracingAspect extends CommonTraceLoggerAspect {

    @Pointcut("execution(* com.brainstation23.skeleton.presenter.rest.api..*(..)))")
    public void skeletonControllerAspect() {
    }

    @Pointcut("execution(* com.brainstation23.skeleton.core.service..*.*(..)))")
    public void skeletonServiceTrace() {
    }

    @Around("skeletonServiceTrace() && !noLogging()")
    public Object logService(final ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

    @Around("skeletonControllerAspect()")
    public Object logController(final ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
