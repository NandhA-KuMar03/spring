package com.application.gatekeeper.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAspect {
    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut(value="execution(* com.application.gatekeeper.controller.*.*(..) )")
    public void controllerPointCut() {
    }

    @Pointcut(value="execution(* com.application.gatekeeper.service.*.*(..) )")
    public void servicePointCut() {
    }

    @Pointcut(value="execution(* com.application.gatekeeper.repository.*.*(..) )")
    public void repositoryPointCut() {
    }

    @Around("controllerPointCut() || servicePointCut() || repositoryPointCut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        log.info("method invoked " + className + " : " + methodName + "()" + "arguments : "
                + mapper.writeValueAsString(array));
        Object object = pjp.proceed();
        log.info(className + " : " + methodName + "()" + "Response : "
                + mapper.writeValueAsString(object));
        return object;
    }
}
