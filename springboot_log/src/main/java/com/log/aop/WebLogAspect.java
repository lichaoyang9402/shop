package com.log.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.log.annotation.Loggable;
import com.log.enums.LogScopeEnum;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.bytecode.LocalVariableAttribute;

/**
 * 日志记录AOP实现
 * create by zhangshaolin on 2018/5/1
 */
//@Aspect
//@Component
public class WebLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    // 开始时间
    private long startTime = 0L;

    // 结束时间
    private long endTime = 0L;

    /**
     * Controller层切点
     */
    @Pointcut("execution(* *..controller..*.*(..))")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
    }

    /**
     * 配置controller环绕通知,使用在方法aspect()上注册的切入点
     *
     * @param point 切点
     * @return
     * @throws Throwable
     */
    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //目标方法实体
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        boolean hasMethodLogAnno = method
                .isAnnotationPresent(Loggable.class);
        //没加注解 直接执行返回结果
        if (!hasMethodLogAnno) {
            return point.proceed();
        }

        //日志打印外部开关默认关闭

        //记录日志信息

        //方法注解实体
        Loggable methodLogAnnon = method.getAnnotation(Loggable.class);
        
        //处理入参日志
//        handleRequstLog(point, methodLogAnnon, request, logMessage, logSwitch);
        
        //执行目标方法内容，获取执行结果
        Object result = point.proceed();
        
        //处理接口响应日志
//        handleResponseLog(logSwitch, logMessage, methodLogAnnon, result);
        return result;
    }
    
    /**
     * 处理入参日志
     *
     * @param point          　切点
     * @param methodLogAnnon 　日志注解
     * @param logMessage     　日志信息记录实体
     */
    private void handleRequstLog(ProceedingJoinPoint point, Loggable methodLogAnnon, HttpServletRequest request,
                                 LogMessage logMessage, String logSwitch) throws Exception {

        String paramsText = "";
        //参数列表
        String includeParam = methodLogAnnon.include();
        Map<String, Object> methodParamNames = getMethodParamNames(
                point.getTarget().getClass(), point.getSignature().getName(), includeParam);
//        Map<String, Object> params = getArgsMap(
//                point, methodParamNames);
//        if (params != null) {
//            //序列化参数列表
//            paramsText = JSON.toJSONString(params);
//        }
//        logMessage.setParameter(paramsText);
//        //判断是否输出日志
//        if (methodLogAnnon.loggable()
//                && methodLogAnnon.scope().contains(LogScopeEnum.BEFORE)
//                && methodLogAnnon.console()
//                && StringUtils.equals(logSwitch, BaseConstants.YES)) {
//            //打印入参日志
//            LOGGER.info("【{}】 接口入参成功!, 方法名称:【{}】, 请求参数:【{}】", methodLogAnnon.descp().toString(), point.getSignature().getName(), paramsText);
//        }
//        startTime = System.currentTimeMillis();
//        //接口描述
//        logMessage.setDescription(methodLogAnnon.descp().toString());
        
        //...省略部分构造logMessage信息代码
    }

    /**
     * 处理响应日志
     *
     * @param logSwitch         外部日志开关，用于外部动态开启日志打印
     * @param logMessage        日志记录信息实体
     * @param methodLogAnnon    日志注解实体
     * @param result         　 接口执行结果
     */
    private void handleResponseLog(String logSwitch, LogMessage logMessage, Loggable methodLogAnnon, Object result) {
        endTime = System.currentTimeMillis();
        //结束时间
        //消耗时间
        //是否输出日志
        if (methodLogAnnon.loggable()
                && methodLogAnnon.scope().contains(LogScopeEnum.AFTER)) {
            //判断是否入库
            if (methodLogAnnon.db()) {
                //...省略入库代码
            }
            //判断是否输出到控制台
//            if (methodLogAnnon.console() 
//                    && StringUtils.equals(logSwitch, BaseConstants.YES)) {
//                //...省略打印日志代码
//            }
        }
    }
    /**
     * 获取方法入参变量名
     *
     * @param cls        触发的类
     * @param methodName 触发的方法名
     * @param include    需要打印的变量名
     * @return
     * @throws Exception
     */
    private Map<String, Object> getMethodParamNames(Class cls,
                                                    String methodName, String include) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(cls));
        CtMethod cm = pool.get(cls.getName()).getDeclaredMethod(methodName);
        LocalVariableAttribute attr = (LocalVariableAttribute) cm
                .getMethodInfo().getCodeAttribute()
                .getAttribute(LocalVariableAttribute.tag);

        if (attr == null) {
            throw new Exception("attr is null");
        } else {
            Map<String, Object> paramNames = new HashMap<>();
            int paramNamesLen = cm.getParameterTypes().length;
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            if (StringUtils.isEmpty(include)) {
                for (int i = 0; i < paramNamesLen; i++) {
                    paramNames.put(attr.variableName(i + pos), i);
                }
            } else { // 若include不为空
                for (int i = 0; i < paramNamesLen; i++) {
                    String paramName = attr.variableName(i + pos);
                    if (include.indexOf(paramName) > -1) {
                        paramNames.put(paramName, i);
                    }
                }
            }
            return paramNames;
        }
    }

    /**
     * 组装入参Map
     *
     * @param point　　　　　　　切点
     * @param methodParamNames　参数名称集合
     * @return
     */
    private Map getArgsMap(ProceedingJoinPoint point,
                           Map<String, Object> methodParamNames) {
        Object[] args = point.getArgs();
        if (null == methodParamNames) {
            return Collections.EMPTY_MAP;
        }
        for (Map.Entry<String, Object> entry : methodParamNames.entrySet()) {
            int index = Integer.valueOf(String.valueOf(entry.getValue()));
            if (args != null && args.length > 0) {
                Object arg = (null == args[index] ? "" : args[index]);
                methodParamNames.put(entry.getKey(), arg);
            }
        }
        return methodParamNames;
    }
}
