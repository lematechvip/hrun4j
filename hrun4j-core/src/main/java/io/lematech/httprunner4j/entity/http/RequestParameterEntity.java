package io.lematech.httprunner4j.entity.http;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * request parameter
 *
 * @version 1.0.0
 * @author: chenfanghang@foxmail.com
 */
@Data
public class RequestParameterEntity<T> {
    private Map<String, Object> params;
    private T data;
    private JSONObject json;
    private Map<String, File> files;
}
