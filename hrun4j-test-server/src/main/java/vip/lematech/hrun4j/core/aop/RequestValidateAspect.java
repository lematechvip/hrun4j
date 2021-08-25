package vip.lematech.hrun4j.core.aop;

import cn.hutool.core.util.StrUtil;
import vip.lematech.hrun4j.core.annotation.ValidateRequest;
import vip.lematech.hrun4j.core.enums.CommonBusinessCode;
import vip.lematech.hrun4j.core.exception.PlatformException;
import vip.lematech.hrun4j.service.TokenService;
import vip.lematech.hrun4j.core.helper.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
@Slf4j
@Aspect
@Component
public class RequestValidateAspect {
    @Autowired
    private TokenService tokenServiceImpl;

    @Before("@within(validateRequest)")
    public void doBeforeForClass(ValidateRequest validateRequest) {
        doBefore(validateRequest);
    }

    @Before("@annotation(validateRequest)")
    public void doBefore(ValidateRequest validateRequest) {
        HttpServletRequest request = ApplicationContextHelper.getHttpServletRequest();
        if (Objects.isNull(request)) {
            log.info("without request,skip");
            return;
        }
        String[] headerNames = validateRequest.headerNames();
        for (String headerName : headerNames) {
            String headerValue = request.getHeader(headerName);
            if (StrUtil.isNotBlank(headerValue)) {
                continue;
            }
            String exceptionMsg = String.format(" header %s is required", headerName);
            throw new PlatformException(exceptionMsg);
        }
        String deviceSN = request.getHeader("device_sn");
        String actualToken = request.getHeader("token");
        String expectToken = tokenServiceImpl.queryToken(deviceSN);
        if (!tokenServiceImpl.validateToken(actualToken, expectToken)) {
            throw new PlatformException(CommonBusinessCode.Authorization_FAILED_EXCEPTION);
        }
    }

}
