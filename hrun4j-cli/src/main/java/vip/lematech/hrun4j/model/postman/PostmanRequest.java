package vip.lematech.hrun4j.model.postman;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
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
