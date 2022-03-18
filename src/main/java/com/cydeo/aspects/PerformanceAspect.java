package com.cydeo.aspects;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Log4j2
public class PerformanceAspect {

    @Pointcut("@within(com.cydeo.annotations.ExecutionTime)")
    private void performancePointCut() {
    }

    @Around("performancePointCut()")
    public Object executionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        log.info("Execution time : {} ms - (Method : {}  - Result : {})", (end - start),
                proceedingJoinPoint.getSignature().toShortString(), result);

        return result;
    }


}
