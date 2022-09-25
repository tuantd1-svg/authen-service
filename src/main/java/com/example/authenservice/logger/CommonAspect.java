package com.example.authenservice.logger;

import com.example.loggerapi.utils.LoggerUtils2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CommonAspect {
    private static final Logger logger = LoggerFactory.getLogger(CommonAspect.class);
    @Autowired
    private ObjectMapper mapper;

    @Pointcut("within(com.example.authenservice.controller..*) " +
            "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)"
            +"|| within(@org.springframework.web.bind.annotation.RestController *)"
            )
    public void pointcut() {
    }
    @Before("pointcut()")
    public void logMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);
        Map<String, Object> parameters = getParameters(joinPoint);
        try {
            LoggerUtils2.info(joinPoint.getSignature().getDeclaringType(),mapping.method()[0].name(),"handle_in" ,mapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }finally {
            MDC.clear();
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, Object entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);
        try {
            LoggerUtils2.info(joinPoint.getSignature().getDeclaringType(), mapping.method()[0].name(),"handler_out",mapper.writeValueAsString(entity));

        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LoggerUtils2.error(joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(),"exception", e.getCause() != null ? e.getMessage() : "NULL");
    }
    @Around(" pointcut()")
    public void invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        final String joinPoints = Arrays.toString(joinPoint.getArgs());
        if (joinPoints != null) {
            LoggerUtils2.info(joinPoint.getSignature().getDeclaringType(),  joinPoint.getSignature().getName(),"send",mapper.writeValueAsString(joinPoints));
        }
        final Object result = joinPoint.proceed();;
        LoggerUtils2.info(joinPoint.getSignature().getDeclaringType(),  joinPoint.getSignature().getName(),"received",mapper.writeValueAsString(result.toString()));
    }
    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();
        
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }
        return map;
    }
}
