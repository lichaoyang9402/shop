package com.log.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.log.annotation.CostLogger;


/***
 * 找个是钱程找的
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月10日 下午3:01:14  
 * @类描述: 
 *
 */

@Component
@Aspect
public class CostLoggerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CostLoggerInterceptor.class);

    @Around("@annotation(com.log.annotation.CostLogger)")
    public Object costLogger(ProceedingJoinPoint pjp) throws Throwable {
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        Object obj = null;

        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("this annotation only used on the method");
        }

        msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        CostLogger costLogger = null;
        try {
            //获取当前方法
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            //获取costLogger注解
            costLogger = currentMethod.getAnnotation(CostLogger.class);
            //当有costLogger注解时
            if (costLogger != null) {
                //开始时间
                long startTime = System.currentTimeMillis();
                try {
                    //执行方法
                    obj = pjp.proceed();
                } catch (Throwable throwable) {
                    throw throwable;
                }
                //耗时
                long cost = System.currentTimeMillis() - startTime;

                //当设置了超时时间，但耗时小于超时时间，不进行日志打印，直接返回 （只有耗时大于超时时间才进行日志打印）
                if (costLogger.timeout() != -1 && cost < costLogger.timeout()) {
                    return obj;
                }

                //方法名
                String methodName = null;
                if (StringUtils.isEmpty(costLogger.methodName())) {
                    //当methodName为默认值时，用signature(原方法名)代替
                    methodName = pjp.getSignature().toString();
                } else if (costLogger.methodName() != null) {
                    //有值则用值
                    methodName = costLogger.methodName();
                }

                //备注
                String remark = null;
                /*if (StringUtils.isEmpty(costLogger.remark())) {
                    remark = "";
                } else if (costLogger.remark() != null) {
                    //有值则用值
                    remark = " [" + costLogger.remark() + "]";
                }*/
                if (StringUtils.isEmpty(costLogger.value())) {
                    remark = "";
                } else if (costLogger.value() != null) {
                    //有值则用值
                    remark = " [" + costLogger.value() + "]";
                }
                //打印内容
                String printContent = null;
                switch (costLogger.LANGUAGE()){
                    case CN:
                        printContent=methodName+ " 耗时 " + cost + " 毫秒 "+remark;
                        break;
                    case EN:
                        printContent=methodName+ " cost " + cost + " ms "+remark;
                        break;
                }

                //打印级别
                if(costLogger.LEVEL().equals(CostLogger.Level.TRACE)){
                    logger.trace(printContent);
                }else if(costLogger.LEVEL().equals(CostLogger.Level.DEBUG)){
                    logger.debug(printContent);
                }else if(costLogger.LEVEL().equals(CostLogger.Level.INFO)){
                    logger.info(printContent);
                }else if(costLogger.LEVEL().equals(CostLogger.Level.WARN)){
                    logger.warn(printContent);
                }else if(costLogger.LEVEL().equals(CostLogger.Level.ERROR)){
                    logger.error(printContent);
                }else{
                    logger.info(printContent);
                }
                System.out.println(printContent);
            } else {
                try {
                    obj = pjp.proceed();
                } catch (Throwable throwable) {
                    throw throwable;
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return obj;

    }
}
