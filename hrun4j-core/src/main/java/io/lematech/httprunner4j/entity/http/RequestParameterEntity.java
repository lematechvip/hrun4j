package io.lematech.httprunner4j.entity.http;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * @version 1.0.0
 * @author: chenfanghang@foxmail.com
 * @date: 2021/4/17
 * @description: request parameter
 */
@Data
public class RequestParameterEntity<T> {
    private Map<String, Object> params;
    private T data;
    private JSONObject json;
}
