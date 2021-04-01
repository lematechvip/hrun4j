package io.lematech.httprunner4j.entity.http;

import lombok.Data;

import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ResponseEntity
 * @description TODO
 * @created 2021/1/27 10:06 上午
 * @publicWechat lematech
 */
@Data
public class ResponseEntity {
    /**
     * status code
     */
    private Integer statusCode;
    /**
     * response headers
     */
    private Map<String, String> headers;
    /**
     * response time
     */
    private Double time;
    /**
     * response content
     */
    private Object content;
    /**
     * response cookies
     */
    private Map<String, String> cookies;

    private Long contentLength;
}
