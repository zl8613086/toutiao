package com.zl.aspect;




import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by zl on 2016/6/27.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.zl.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinpoint){
        logger.info("before method:");
    }
    @After("execution(* com.zl.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinpoint){
        logger.info("after method:");
    }
}
