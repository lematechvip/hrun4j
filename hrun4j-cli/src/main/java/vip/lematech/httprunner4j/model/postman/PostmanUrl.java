package vip.lematech.httprunner4j.model.postman;

import lombok.Data;

import java.util.List;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
@Data
public class PostmanUrl {

    private String raw;
    private String protocol;
    private List<String> host;
    private String port;
    private List<String> path;
    private List<PostmanKeyValue> query;
}
