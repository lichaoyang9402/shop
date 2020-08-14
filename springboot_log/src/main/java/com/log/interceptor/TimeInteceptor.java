package com.log.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**   
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月9日 下午4:12:06  
 * @类描述: 
 *
 */
@Component
public class TimeInteceptor implements HandlerInterceptor {
        //无论controller中是否抛出异常，都会调用该方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception ex)
            throws Exception {
        System.out.println("afterCompletion");
        long start=new Date().getTime();
        System.out.println("最后耗时为:"+(new Date().getTime()-start));
        System.out.println("ex is"+ex);
    }
    //如果controller中抛出异常，则该方法不会被调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView view)
            throws Exception {
//      ModelMap map=new ModelMap();
//      map.addAttribute("name", "shinelon");
        System.out.println("postHandle");
        long start=(long) request.getAttribute("start");
        System.out.println("消耗的时间为:"+(new Date().getTime()-start));

    }
    //最先执行该方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        System.out.println("preHandle");
        System.out.println(((HandlerMethod)obj).getBean().getClass().getName());
        System.out.println(((HandlerMethod)obj).getMethod().getName());
        request.setAttribute("start", new Date().getTime());
        System.out.println("开始时间:"+new Date().getTime());
        return true;
    }

}
