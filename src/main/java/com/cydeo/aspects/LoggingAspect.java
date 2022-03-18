package com.cydeo.aspects;

import com.cydeo.dto.BatchDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {


    @Pointcut("execution(* com.cydeo.controller.*.*(..))")
    private void allController() {
    }

    @Before("allController()")
    public void allControllerAdvice(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Before for Controller Methods -> Method : {} - Args : {} - user info : {}",joinPoint.getSignature().toShortString(),joinPoint.getArgs() ,authentication.getName());
    }

}
