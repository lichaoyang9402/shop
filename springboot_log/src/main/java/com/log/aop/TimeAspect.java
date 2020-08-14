package com.log.aop;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**** 

	 <dependency>
	    	<groupId>org.springframework.boot</groupId>
	    	<artifactId>spring-boot-starter-aop</artifactId>
	 </dependency>
 *
 */
//@Aspect
//@Component
public class TimeAspect {

    @Around("execution(* com.log.controller.*.*(..))")
    public Object AspectHandlerMethod11(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("apect 开始。。。。。。。。。。。。。。。。");
        long start=new Date().getTime();
        Object[] args=pjp.getArgs();
        for(Object arg:args) {
            System.out.println("arg is "+arg);
        }
        
        // 获取HttpServletRequest
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        
        ServletRequestAttributes   attributes    = (ServletRequestAttributes)requestAttributes;
        
        HttpServletRequest request = attributes.getRequest();
        
        // uri
        String requestURI = request.getRequestURI();
        
        System.out.println(requestURI);
        
        // 方法实体
        Method method = ((MethodSignature)pjp.getSignature()).getMethod();
        
        System.out.println("方法名称： "+method);
        
        String declaringClassName = method.getDeclaringClass().getName();
        
        System.out.println(declaringClassName);
        
        System.out.println("handler: "+declaringClassName+"."+method.getName()+"()");
        
        
        System.out.println("最后耗时为:"+(new Date().getTime()-start));
        
        Object obj=pjp.proceed();
        System.out.println("aspect 结束。。。。。。。。。。。。。。。。。");
        return obj;
    }
}