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
 * @description api model define
 * @created 2021/3/16 11:13 上午
 * @publicWechat lematech
 */
@Data
public class ApiModel<T> extends BaseModel {
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "base_url")
    private String baseUrl;
    /**
     * array[map<k,array>]：used to validate response fields,validate_func_name: [check_value, expect_value]
     * - equalTo: [statusCode, "200"]
     * array[map<k,array>]：one validator definition(TP)
     * - "check": "statusCode"
     * "comparator": "equalTo"
     * "expect": "200"
     */
    private List<Map<String, Object>> validate;
    private RequestEntity request;
    private T extract;
}
