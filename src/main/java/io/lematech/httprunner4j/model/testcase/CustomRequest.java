package io.lematech.httprunner4j.model.testcase;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.HashMap;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className CustomRequest
 * @description TODO
 * @created 2021/1/20 5:01 下午
 * @publicWechat lematech
 */
@Data
public class CustomRequest<T> {
    private String method;
    private String url;
    /**
     * TODO: 2021/1/20 可选键值对（对象）
     * 1、query string for request url
     */
    private HashMap<String,Object> params;
    /**
     * TODO: 2021/1/20 可选键值对、字符串（包含变量引用或方法引用字符串），暂定义为键值对
     * 1、request body in json format
     * 2、request body in application/x-www-form-urlencoded format
     * 3、request body prepared with function, or reference a variable
     */
    private T data;

    /**
     * TODO: 2021/1/20 可选键值对、字符串（包含变量引用或方法引用字符串），暂定义为键值对
     * 1、request body in json format
     * 2、request body prepared with function, or reference a variable
     */
    private JSONObject json;
    /**
     * TODO: 2021/1/20 可选键值对且变量值为字符串（包含变量引用或方法引用字符串），暂定义为键值对
     */
    private HashMap<String,String> headers;

    /**
     * How many seconds to wait for the server to send data before giving up
     */
    private Double timeout;
    /**
     * Enable/disable GET/OPTIONS/POST/PUT/PATCH/DELETE/HEAD redirection. Defaults to True
     */
    private Boolean allowRedirects = true;

    /**
     * request cookies
     */
    private Object cookies;

    /**
     * request files, used to upload files
     */
    private Object files;
    /**
     *
     */
    private Object auth;
    /**
     * Dictionary mapping protocol to the URL of the proxy
     */
    private Object proxies;
    /**
     * upload files
     */
    private Object upload;

    /**
     * configure verify for current api/teststep
     */
    private Boolean verify;
    /**
     * if False, the response content will be immediately downloaded.
     */
    private Boolean stream;

}
