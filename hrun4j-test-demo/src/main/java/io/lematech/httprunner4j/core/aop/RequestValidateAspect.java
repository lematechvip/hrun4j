package io.lematech.httprunner4j.core.aop;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.core.annotation.ValidateRequest;
import io.lematech.httprunner4j.core.enums.CommonBusinessCode;
import io.lematech.httprunner4j.core.exception.PlatformException;
import io.lematech.httprunner4j.core.util.ApplicationContextUtil;
import io.lematech.httprunner4j.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RequestValidateAspect
 * @description TODO
 * @created 2021/3/3 11:36 上午
 * @publicWechat lematech
 */
@Slf4j
@Aspect
@Component
public class RequestValidateAspect {
    @Autowired
    private TokenService tokenServiceImpl;
    @Before("@within(validateRequest)")
    public void doBeforeForClass(ValidateRequest validateRequest){
        doBefore(validateRequest);
    }

    @Before("@annotation(validateRequest)")
    public void doBefore(ValidateRequest validateRequest){
        HttpServletRequest request = ApplicationContextUtil.getHttpServletRequest();
        if(Objects.isNull(request)){
            log.info("without request,skip");
            return;
        }
        String []headerNames = validateRequest.headerNames();
        for(String headerName : headerNames){
            String headerValue = request.getHeader(headerName);
            if(StrUtil.isNotBlank(headerValue)){
                continue;
            }
            String exceptionMsg = String.format(" header %s is required",headerName);
            throw new PlatformException(exceptionMsg);
        }
        String deviceSN = request.getHeader("device_sn");
        String actaulToken = request.getHeader("token");
        String expectToken = tokenServiceImpl.queryToken(deviceSN);
        if(!tokenServiceImpl.validateToken(actaulToken,expectToken)){
            throw new PlatformException(CommonBusinessCode.Authorization_FAILED_EXCEPTION);
        }
    }

}
