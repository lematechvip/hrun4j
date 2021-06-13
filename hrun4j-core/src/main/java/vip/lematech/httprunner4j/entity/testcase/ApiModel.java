package vip.lematech.httprunner4j.entity.testcase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import vip.lematech.httprunner4j.entity.http.RequestEntity;
import vip.lematech.httprunner4j.entity.base.BaseModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
