package ru.pozhar.collector_api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {
    @Pointcut("within(ru.pozhar.collector_api..*)")
    public void globalPointcut() { }

    @Around("globalPointcut()")
    public Object logException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String method = joinPoint.getSignature().getName();
            String exClassName = ex.getClass().getSimpleName();
            String exMessage = ex.getMessage();
            log.error("Exception in {}.{}(): {} - {}", className, method, exClassName, exMessage, ex);
            throw ex;
        }
    }
}
