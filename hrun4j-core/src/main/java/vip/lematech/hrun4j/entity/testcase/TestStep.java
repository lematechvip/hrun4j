package vip.lematech.hrun4j.entity.testcase;

import lombok.*;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.base.BaseModel;

import java.util.List;
import java.util.Map;


/**
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestStep<T> extends BaseModel{

    private String name;
    /**
     * used to extract session variables for later requests
     * map<k,v>：extraction rule for session variable, maybe in jsonpath/regex/jmespath（TP）
     * code__by_jsonpath: $.code
     * item_id__by_jsonpath: $..items.*.id
     * var_name__by_regex: '"LB[\d]*(.*)RB[\d]*"'
     * content_type: headers.content-type
     * first_name: content.person.name.first_name
     * array[map<k,v>]：
     * -code__by_jsonpath: $.code
     * -item_id__by_jsonpath: $..items.*.id
     * -var_name__by_regex: '"LB[\d]*(.*)RB[\d]*"'
     * -content_type: headers.content-type
     * -first_name: content.person.name.first_name
     */
    private T extract;

    /**
     * array[map<k,array>]：used to validate response fields,validate_func_name: [check_value, expect_value],comparator default set equalTo
     * - equalTo: [statusCode, "200"]
     * array[map<k,array>]：one validator definition(TP)
     * - "check": "statusCode"
     * "comparator": "equalTo"
     * "expect": "200"
     */
    private List<Map<String,Object>> validate;

    /**
     * http request
     */
    private RequestEntity request;

    /**
     * api reference, value is api file relative path
     */
    private String api;

    /**
     * testcase reference
     */
    private String testcase;

    /**
     * The list of arguments that the entire use case outputs. The arguments that can be output include the common variable and extract arguments
     */
    private List output;
}
