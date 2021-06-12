package vip.lematech.httprunner4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vip.lematech.httprunner4j.entity.base.BaseModel;
import vip.lematech.httprunner4j.entity.http.RequestEntity;

import java.util.List;
import java.util.Map;

/**
 * website http://lematech.vip/
 *
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
@Data
public class ApiModel<T> extends BaseModel {
    @JsonProperty(value = "name")
    @JSONField(ordinal = 1)
    private String name;
    @JsonProperty(value = "base_url")
    @JSONField(ordinal = 2)
    private String baseUrl;

    /**
     * array[map<k,array>]：used to validate response fields,validate_func_name: [check_value, expect_value]
     * - equalTo: [statusCode, "200"]
     * array[map<k,array>]：one validator definition(TP)
     * - "check": "statusCode"
     * "comparator": "equalTo"
     * "expect": "200"
     */
    @JSONField(ordinal = 5)
    private List<Map<String, Object>> validate;
    @JSONField(ordinal = 3)
    private RequestEntity request;
    @JSONField(ordinal = 4)
    private T extract;
}
