package vip.lematech.httprunner4j.model.postman;

import lombok.Data;

import java.util.List;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
@Data
public class PostmanItem {
    private String name;
    private List<PostmanEvent> event;
    private PostmanRequest request;
    private List<PostmanItem> item;
    private List<PostmanResponse> response;
}
