package vip.lematech.hrun4j.entity.http;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.util.Map;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity<T> {
    @JSONField(ordinal = 2)
    private String method;
    @JSONField(ordinal = 1)
    private String url;
    /**
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
    private Integer connectTimeout;

    /**
     * Enable/disable GET/OPTIONS/POST/PUT/PATCH/DELETE/HEAD redirection. Defaults to True
     */
    @JSONField(ordinal = 11)
    private Boolean allowRedirects = true;
    @JSONField(ordinal = 9)
    private Integer writeTimeout;

    @JSONField(ordinal = 10)
    private Integer readTimeout;


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
