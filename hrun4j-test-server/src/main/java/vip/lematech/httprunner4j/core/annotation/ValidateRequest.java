package vip.lematech.httprunner4j.core.annotation;

import java.lang.annotation.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ValidateRequest
 * @description TODO
 * @created 2021/3/3 11:23 上午
 * @publicWechat lematech
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateRequest {
    /**
     * Without default value means this argument is required
     *
     * @return Header names
     */
    String[] headerNames();

}
