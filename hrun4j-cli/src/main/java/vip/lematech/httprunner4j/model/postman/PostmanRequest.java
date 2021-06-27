package vip.lematech.httprunner4j.model.postman;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
@Data
public class PostmanRequest {

    private String method;
    private String schema;
    private List<PostmanKeyValue> header;
    private JSONObject body;
    private JSONObject auth;
    private PostmanUrl url;
    private String description;
}
