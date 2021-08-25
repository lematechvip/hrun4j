package vip.lematech.hrun4j.model.postman;


import lombok.Data;

import java.util.List;

/**
 * website https://www.lematech.vip/
 * @author chenfanghang
 * @version 1.0.1
 */
@Data
public class PostmanResponse {
    private String name;
    private String status;
    private String code;
    private String _postman_previewlanguage;
    private List<PostmanKeyValue> header;
    private String body;
}
