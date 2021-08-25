package vip.lematech.hrun4j.entity.http;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Data
public class ResponseEntity {
    /**
     * status code
     */
    @JSONField(name = "status_code")
    private Integer statusCode;
    /**
     * response headers
     */
    private Map<String, Object> headers;
    /**
     * response time
     */
    private Double time;

    /**
     * response content
     */
    @JSONField(name = "body")
    private Object body;
    /**
     * response cookies
     */
    private Map<String, Object> cookies;

    /**
     * response content length
     */
    private Long contentLength;
}
