package io.lematech.httprunner4j.handler;

import io.lematech.httprunner4j.model.testcase.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Executor
 * @description TODO
 * @created 2021/1/22 10:49 上午
 * @publicWechat lematech
 */
public class Executor {
    private TestCase testCase;
    private Handler handler = new Handler();
    private Map<String,Object> globalParamsMap = new HashMap<>();
    public void execute(String testcaseName){
         this.testCase = handler.load(testcaseName);
         execute();
    }
    private void execute(){

    }

}
