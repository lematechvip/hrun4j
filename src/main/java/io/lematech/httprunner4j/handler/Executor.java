package io.lematech.httprunner4j.handler;


import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.utils.ExpressHandler;
import io.lematech.httprunner4j.utils.HttpClientUtil;
import io.lematech.httprunner4j.utils.MyHttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Executor
 * @description TODO
 * @created 2021/1/22 10:49 上午
 * @publicWechat lematech
 */
@Slf4j
public class Executor {
    private TestCase testCase;
    private ExpressHandler expressHandler;
    private Handler handler = new Handler();
    private Map<String,Object> globalParamsMap = new HashMap<>();
    public void execute(String testcaseName){
         this.testCase = handler.load(testcaseName);
         expressHandler = new ExpressHandler();
         execute();
    }

    private void execute(){
        Config config = testCase.getConfig();
        List<TestStep> testSteps  = testCase.getTestSteps();
        RequestEntity requestEntity = new RequestEntity();
        for(TestStep testStep : testSteps){
            String url = String.format("%s%s",config.getBaseUrl(),testStep.getRequest().getUrl());
            //数据包裹
            //请求处理
            String method =testStep.getRequest().getMethod();
            log.info("===============================用例执行开始===============================");
            HashMap env = new HashMap(globalParamsMap);
            Map<String, String> headers = (Map)expressHandler.handleExpress(testStep.getRequest().getHeaders(),env);
            Map<String, Object> params = (Map)expressHandler.handleExpress(testStep.getRequest().getParams(),env);
            ResponseEntity responseEntity = MyHttpClient.executeReq(requestEntity);
            log.info("响应信息：{}", responseEntity);
            log.info("===============================用例执行结束===============================");
            //结果验证
            //参数提取

        }
    }

}
