package ru.pozhar.collector_api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodLoggingAspect {
    @Before("execution(* ru.pozhar.collector_api..*(..))")
    public void logMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Вызван метод: {}.{} аргументы {}", className, method, args);
    }
}
