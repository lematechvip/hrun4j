package vip.lematech.hrun4j.model.postman;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
 */
@Data
public class PostmanCollectionInfo {

    @JSONField(name = "_postman_id")
    private String postmanId;
    private String name;
    private String schema;
}
