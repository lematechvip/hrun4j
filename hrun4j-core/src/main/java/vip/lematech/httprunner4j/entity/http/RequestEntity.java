package vip.lematech.httprunner4j.entity.http;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import vip.lematech.httprunner4j.widget.utils.SmallUtil;

import java.util.Map;

/**
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
@Data
public class RequestEntity<T> {
    @JSONField(ordinal = 2)
    private String method;
    @JSONField(ordinal = 1)
    private String url;
    /**
     * TODO: 2021/1/20 可选键值对（对象）
     * 1、query string for request url
     */
    @JSONField(ordinal = 4)
    private Map<String,Object> params;
    /**
     * 1、request body in json format
     * 2、request body in application/x-www-form-urlencoded format
     * 3、request body prepared with function, or reference a variable
     */
    @JSONField(ordinal = 6)
    private T data;

    /**
     * 1、request body in json format
     * 2、request body prepared with function, or reference a variable
     */
    @JSONField(ordinal = 5)
    private T json;

    @JSONField(ordinal = 3)
    private Map<String, Object> headers;

    @JSONField(ordinal = 8)
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

    @JSONField(ordinal = 9)
    private Integer connectTimeout;

    @JSONField(ordinal = 10)
    private Integer socketTimeout;

    /**
     * Enable/disable GET/OPTIONS/POST/PUT/PATCH/DELETE/HEAD redirection. Defaults to True
     */
    @JSONField(ordinal = 11)
    private Boolean allowRedirects = true;

    /**
     * request cookies
     */
    @JSONField(ordinal = 12)
    private Map<String, Object> cookies;

    /**
     * request files, used to upload files
     */
    @JSONField(ordinal = 7)
    private Object files;
    /**
     *
     */
    @JSONField(ordinal = 13)
    private Object auth;
    /**
     * Dictionary mapping protocol to the URL of the proxy
     */
    @JSONField(ordinal = 14)
    private Map<String, Object> proxy;

    /**
     * if False, the response content will be immediately downloaded.
     */
    @JSONField(ordinal = 15)
    private Boolean stream = true;

}
