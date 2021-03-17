package io.lematech.httprunner4j.entity.testcase;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.lematech.httprunner4j.entity.base.BaseModel;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ApiModel
 * @description TODO
 * @created 2021/3/16 11:13 上午
 * @publicWechat lematech
 */
@Data
public class ApiModel extends BaseModel {
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "base_url")
    private String baseUrl;
    /**
     * TODO: 2021/1/20 可选值：键值对（值可能是内置对象）或者纯对象
     */
    private List<Map<String, Object>> validate;
    private RequestEntity request;

}
