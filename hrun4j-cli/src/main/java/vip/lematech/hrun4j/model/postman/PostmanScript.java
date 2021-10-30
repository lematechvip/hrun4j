package vip.lematech.hrun4j.model.postman;

import lombok.Data;

import java.util.List;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
 */
@Data
public class PostmanScript {
    private List<String> exec;
    private String type;
}
