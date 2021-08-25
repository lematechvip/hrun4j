package vip.lematech.hrun4j.core.annotation;

import java.lang.annotation.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
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
