package io.lematech.httprunner4j.entity.testcase;

import io.lematech.httprunner4j.entity.base.BaseModel;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestStep
 * @description 1、http请求/2、关联其他用例/3、api reference, value is api file relative path
 * @created 2021/1/20 2:34 下午
 * @publicWechat lematech
 */
@Data
public class TestStep<T> extends BaseModel{

    private String name;
    /**
     * map<k,v>：
     * -code__by_jsonpath: $.code
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
     *  array[map<k,v>]：extract variables,comparator defautl set equalTo
     *     - check: body.code
     *       comparator: gt
     *       expect: 0
     *  array[map<k,array>]：extract variables,comparator defautl set equalTo
     *    eq:
     *      - status_code
     *      - 200
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

}
