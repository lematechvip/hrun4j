package vip.lematech.httprunner4j.entity.testcase;

import vip.lematech.httprunner4j.entity.base.BaseModel;
import vip.lematech.httprunner4j.entity.http.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;


/**
 *
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
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
     * 1、http请独有
     */
    private RequestEntity request;

    /**
     * 3、api reference, value is api file relative path
     */
    private String api;

    /**
     * testcase reference
     */
    private String testcase;

    /**
     * 整个用例输出的参数列表，可输出的参数包括公共的 variable 和 extract 的参数
     */
    private List output;
}
