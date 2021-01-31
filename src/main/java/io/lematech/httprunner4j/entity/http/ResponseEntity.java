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
    private Integer statusCode;
    private Map<String,String> headers;
    private Double responseTime;
    private String responseContent;
    private Map<String,String> cookies;
}
