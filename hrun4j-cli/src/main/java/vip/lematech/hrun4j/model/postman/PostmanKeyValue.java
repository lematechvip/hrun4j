package vip.lematech.hrun4j.model.postman;

import lombok.Data;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
 */
@Data
public class PostmanKeyValue {
    private String key;
    private String value;
    private String type;
    private String description;
    private String contentType;

    public PostmanKeyValue() {
    }

    public PostmanKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
