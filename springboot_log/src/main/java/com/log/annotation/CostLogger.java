package com.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
    public @interface CostLogger{

    //展示语言
    Language LANGUAGE() default Language.EN;

    //日志级别
    Level LEVEL() default Level.DEBUG;

    //方法名 (覆盖默认方法名)
    String methodName() default "";

    //备注
    String remark() default "";

    String value() default "";
    //超时打印 (超过预设的时间才进行日志打印，默认-1为不开启。单位ms)
    int timeout() default -1;


    enum Language{
        EN,CN
    }

    enum Level{
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
