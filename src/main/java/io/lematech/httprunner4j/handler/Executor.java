package io.lematech.httprunner4j.handler;


import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.utils.ExpressHandler;
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
    private Map<String,Object> testContext = new HashMap<>();
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
            //单个用例
            //数据验证 支持全量、单个
            //序列化
            //数据包裹及表达式求值
            //请求处理
            //校验器
            //提取器
            //批量用例
            //借助testng构建测试用例集
            String method =testStep.getRequest().getMethod();
            log.info("===============================用例执行开始===============================");

            Map<String, String> headers = (Map)expressHandler.handleExpress(testStep.getRequest().getHeaders());
            Map<String, Object> params = (Map)expressHandler.handleExpress(testStep.getRequest().getParams());

            //处理参数优先级
            expressHandler.buildCurrentEnv(this.testContext
                    ,config.getVariables()
                    ,testStep.getVariables());
            //表达式求值


            ResponseEntity responseEntity = MyHttpClient.executeReq(requestEntity);
            log.info("响应信息：{}", responseEntity);
            log.info("===============================用例执行结束===============================");
            //结果验证
            //参数提取

        }
    }

}
