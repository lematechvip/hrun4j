package vip.lematech.hrun4j.core.aop;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import vip.lematech.hrun4j.core.helper.ApplicationContextHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */

@Component
@Aspect
@Slf4j
public class WebLogAspect {
    @Pointcut("execution(public * vip.lematech.hrun4j.controller.*.*(..))")
    public void webLog() {

    }

    /**
     * 在切入点之前
     *
     * @param joinPoint 切入点
     */
    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ApplicationContextHelper.getHttpServletRequest();
        log.info("========================================== 请求调用开始 ==========================================");
        log.info("请求地址: {}", request.getRequestURL().toString());
        log.info("请求方法: {}", request.getMethod());
        log.info("请求参数: {}", new Gson().toJson(joinPoint.getArgs()));
        log.info("请求处理器: {}#{}", joinPoint.getSignature().getDeclaringTypeName()
                , joinPoint.getSignature().getName());
        log.info("请求方IP地址: {}", request.getRemoteAddr());
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        log.info("响应参数: {}", new Gson().toJson(result));
        log.info("请求耗时: {} ms", System.currentTimeMillis() - startTime);
        log.info("=========================================== 请求调用结束 ===========================================");
        return result;
    }
}
