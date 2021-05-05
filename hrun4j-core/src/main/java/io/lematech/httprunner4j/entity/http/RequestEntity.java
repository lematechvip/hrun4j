package io.lematech.httprunner4j.entity.http;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.utils.SmallUtil;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RequestEntity
 * @description request entity
 * @created 2021/1/20 5:01 下午
 * @publicWechat lematech
 */
@Data
public class RequestEntity<T> {
    private String method;
    private String url;
    /**
     * TODO: 2021/1/20 可选键值对（对象）
     * 1、query string for request url
     */
    private Map<String,Object> params;
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

    public Map<String, Object> getHeaders() {
        if (!Objects.isNull(this.cookies)) {
            headers.put("Set-Cookie", getCookiesWithParams(this.cookies));
        }
        return headers;
    }

    private String getCookiesWithParams(Map<String, Object> cookies) {
        StringBuilder sb = new StringBuilder();
        if (MapUtil.isNotEmpty(cookies)) {
            for (String key : cookies.keySet()) {
                String value = cookies.get(key).toString();
                try {
                    String sVal = URLEncoder.encode(value, Constant.CHARSET_UTF_8);
                    sb.append(key).append("=").append(sVal).append(";");
                } catch (UnsupportedEncodingException e) {
                    String exceptionMsg = String.format("Parameter %s encoding exception, exception information::%s", value, e.getMessage());
                    throw new DefinedException(exceptionMsg);
                }
            }
        }
        return sb.toString();
    }

    /**
     * TODO: 2021/1/20 可选键值对且变量值为字符串（包含变量引用或方法引用字符串），暂定义为键值对
     */
    private Map<String, Object> headers;

    private Integer connectionRequestTimeout;

    public Integer getConnectionRequestTimeout() {
        return SmallUtil.s2ms(this.connectionRequestTimeout);
    }

    public Integer getConnectTimeout() {
        return SmallUtil.s2ms(this.connectTimeout);
    }

    public Integer getSocketTimeout() {
        return SmallUtil.s2ms(this.socketTimeout);
    }

    private Integer connectTimeout;

    private Integer socketTimeout;

    /**
     * Enable/disable GET/OPTIONS/POST/PUT/PATCH/DELETE/HEAD redirection. Defaults to True
     */
    private Boolean allowRedirects = true;

    /**
     * request cookies
     */
    private Map<String, Object> cookies;

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
    private Map<String, Object> proxy;
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
