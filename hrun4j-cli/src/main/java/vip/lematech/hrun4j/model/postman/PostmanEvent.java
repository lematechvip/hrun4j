package vip.lematech.hrun4j.model.postman;

import lombok.Data;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
 */
@Data
public class PostmanEvent {
    private String listen;
    private PostmanScript script;
}
