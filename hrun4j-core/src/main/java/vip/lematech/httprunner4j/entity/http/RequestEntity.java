package vip.lematech.httprunner4j.entity.http;


import vip.lematech.httprunner4j.helper.LittleHelper;
import lombok.Data;

import java.util.Map;

/**
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
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
     * 1、request body in json format
     * 2、request body in application/x-www-form-urlencoded format
     * 3、request body prepared with function, or reference a variable
     */
    private T data;

    /**
     * 1、request body in json format
     * 2、request body prepared with function, or reference a variable
     */
    private T json;

    private Map<String, Object> headers;

    private Integer connectionRequestTimeout;

    public Integer getConnectionRequestTimeout() {
        return LittleHelper.s2ms(this.connectionRequestTimeout);
    }

    public Integer getConnectTimeout() {
        return LittleHelper.s2ms(this.connectTimeout);
    }

    public Integer getSocketTimeout() {
        return LittleHelper.s2ms(this.socketTimeout);
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
     * if False, the response content will be immediately downloaded.
     */
    private Boolean stream = true;

}
