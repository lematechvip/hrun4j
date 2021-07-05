package vip.lematech.hrun4j.model.postman;

import lombok.Data;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
@Data
public class PostmanEvent {
    private String listen;
    private PostmanScript script;
}
