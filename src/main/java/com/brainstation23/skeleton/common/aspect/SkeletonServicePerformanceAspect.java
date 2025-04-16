package com.brainstation23.skeleton.common.aspect;

import com.brainstation23.skeleton.common.logger.CommonPerformanceLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SkeletonServicePerformanceAspect extends CommonPerformanceLoggerAspect {

    @Pointcut("execution(public * com.brainstation23.skeleton.data.repository..*.*(..))")
    public void skeletonRepositoryPerformanceTrace() {
    }

    @Around("skeletonRepositoryPerformanceTrace())")
    public Object skeletonServicePerformance(final ProceedingJoinPoint joinPoint) throws Throwable {
        return tracePerformance(joinPoint);
    }

}
