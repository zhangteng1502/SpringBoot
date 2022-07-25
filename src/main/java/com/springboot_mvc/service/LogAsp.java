package com.springboot_mvc.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAsp {
    /**
     * 切点
     */
    @Pointcut("@annotation(com.springboot_mvc.service.Log)")
    public void pointCut() {
    }

    /**
     * 环绕日志打印
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Log annotation = method.getAnnotation(Log.class);
        if (null == annotation) {
            return joinPoint.getArgs();
        }
        String name = annotation.name();
        long startTime = System.currentTimeMillis();
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>方法[{}]开始执行,开始时间{}", name, startTime);
        HttpServletRequest request = getRequest();
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>请求方法路径为:{}", request.getRequestURL());
        StringBuilder params = new StringBuilder();
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(argNames[i]).append(":").append(argValues[i]);
            }
        }
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>请求参数为:[{}]", params + "");
        Object proceed = joinPoint.proceed();
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>响应参数为: [{}]",  JSON.toJSON(proceed));
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>执行完毕耗时 :{}", (System.currentTimeMillis() - startTime));
        return proceed;
    }

    /**
     * 异常日志打印
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void throwIng(JoinPoint joinPoint, Throwable e) {
        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>开始进行记录");
        String stackTrace = stackTrace(e);
        HttpServletRequest request = getRequest();
        String ipAddr = IpUtils.getIpAddr(request);
        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>当前请求的Ip地址为:{}", ipAddr);
        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>错误信息为:{}", stackTrace);
        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>异常日志记录完毕");
    }

    /**
     * 堆栈异常获取
     *
     * @param throwable
     * @return
     */
    private static String stackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 获取HttpServletRequest
     *
     * @return
     */
    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        return servletRequestAttributes.getRequest();
    }
}
