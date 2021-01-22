package io.lematech.httprunner4j.model.testcase;

import io.lematech.httprunner4j.model.base.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
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
@NoArgsConstructor
@Data
public class TestStep<T> extends BaseModel{

    private String name;
    /**
     * TODO: 2021/1/20 可选值：键值对（值可能是内置对象）、列表，暂定义为键值对
     */
    private T extract;
    /**
     * TODO: 2021/1/20 可选值：键值对（值可能是内置对象）或者纯对象
     */
    private List<Map<String, Object>> validate;

    /**
     * 1、http请独有
     */
    private CustomRequest request;

    /**
     * 2、关联其他用例，属性独有
     */
    private String testcase;

    /**
     * 3、api reference, value is api file relative path
     */
    private String api;


}
