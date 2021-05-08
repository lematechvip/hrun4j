package io.lematech.httprunner4j.entity.http;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ResponseEntity
 * @description response entity
 * @created 2021/1/27 10:06 上午
 * @publicWechat lematech
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
