package com.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;

@Aspect
@Component
public class RestControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object aroundEndpointAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("\"{}\" started with arguments {}", getEndpoint(joinPoint), joinPoint.getArgs());
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Object proceed = joinPoint.proceed();

        stopWatch.stop();

        logger.info("\"{}\" completed in {} ms", getEndpoint(joinPoint), stopWatch.getTotalTimeMillis());

        return proceed;
    }

    private String getEndpoint(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        GetMapping annotation = method.getAnnotation(GetMapping.class);
        return annotation.value()[0];
    }
}
