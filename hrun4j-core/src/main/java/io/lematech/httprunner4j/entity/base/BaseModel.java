package io.lematech.httprunner4j.entity.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.lematech.httprunner4j.utils.RegularUtil;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className BaseModel
 * @description TODO
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */
@Data
public class BaseModel<T> {

    /**
     * array[string]：call setup hook functions, return nothing
     * - '${sleep(2)}'
     * - '${hook_print(setup)}'
     * - '${modify_request_json($request, android)}'
     * - '${alter_response($response)}'
     * map<k,v>：call setup hook functions, return value and assign to variable
     * - total: '${sum_two(1, 5)}'
     * - filed_name: get_decoded_response_field($response)
     */
    @JsonProperty(value = "setup_hooks")
    private T setupHooks;

    /**
     * array[string]：call teardown hook hook functions, return nothing
     * - '${sleep(2)}'
     * - '${hook_print(setup)}'
     * - '${modify_request_json($request, android)}'
     * - '${alter_response($response)}'
     * map<k,v>：call teardown hook functions, return value and assign to variable
     * - total: '${sum_two(1, 5)}'
     * - filed_name: get_decoded_response_field($response)
     */
    @JsonProperty(value = "teardown_hooks")
    private T teardownHooks;

    /**
     * array[string]：if exp and execute exp, return value and assign to variable
     *    $prepared_variables
     *  map<k,v>：assign to variable
     *    var1: value1
     *    var2: value2
     *  array[map<k,v>]：assign to variable
     *    -var1: value1
     *     var11: value11
     *    -var2: value2
     *     var21: value21
     */
    @JsonProperty(value = "variables")
    private T variables;
}

