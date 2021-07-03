package vip.lematech.httprunner4j.model.postman;

import lombok.Data;

import java.util.List;

/**
 * website http://lematech.vip/
 * @author chenfanghang
 * @version 1.0.0
 */
@Data
public class PostmanCollection {

    private PostmanCollectionInfo info;
    private List<PostmanItem> item;
    private List<PostmanKeyValue> variable;
}
