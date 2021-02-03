package io.lematech.httprunner4j.entity.testcase;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.lematech.httprunner4j.entity.base.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Config
 * @description TODO
 * @created 2021/1/20 2:25 下午
 * @publicWechat lematech
 */
@NoArgsConstructor
@Data
public class Config extends BaseModel {
    /**
     * 必填
     */
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "base_url")
    private String baseUrl;
    @JsonProperty(value = "verify")
    private Boolean verify ;

}
