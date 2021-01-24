package io.lematech.httprunner4j.handler;

import io.lematech.httprunner4j.model.testcase.Config;
import io.lematech.httprunner4j.model.testcase.TestCase;
import io.lematech.httprunner4j.model.testcase.TestStep;
import io.lematech.httprunner4j.utils.PooledHttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
public class Executor {
    private TestCase testCase ;
    private Map<String,Object> globalParamMap = new HashMap();
    private Handler handler = new Handler();
    public void execute(String testCaseName){
        testCase = handler.load(testCaseName);
        execute();
    }

    private void execute(){
        Config config = testCase.getConfig();
        List<TestStep> testSteps  = testCase.getTestSteps();
        for(TestStep testStep : testSteps){
            String url = String.format("%s%s",config.getBaseUrl(),testStep.getRequest().getUrl());
            //数据包裹
            //请求处理
            String method =testStep.getRequest().getMethod();
            log.info("===============================用例执行开始===============================");
            Map<String, String> headers = testStep.getRequest().getHeaders();
            Map<String, Object> params = testStep.getRequest().getParams();
            log.info("请求地址：{},请求方式：GET",url);
            log.info("请求方法：{}",method);
            log.info("请求头信息：{}",headers);
            log.info("请求参数信息：",params);
            if("GET".equalsIgnoreCase(method)){
                String responseValue = PooledHttpClientUtil.getHttpClientInstance().doGet(url,headers,params);
                log.info("响应信息：{}",responseValue);
            }else if ("POST".equalsIgnoreCase(method)){
                String responseValue = PooledHttpClientUtil.getHttpClientInstance().doPost(url,headers,params);
                log.info("响应信息：{}",responseValue);
            }else if ("CONNECT".equalsIgnoreCase(method)){

            }else if ("TRACE".equalsIgnoreCase(method)){

            }else if ("PUT".equalsIgnoreCase(method)){

            }else if ("OPTIONS".equalsIgnoreCase(method)){

            }
            log.info("===============================用例执行结束===============================");
            //结果验证
            //参数提取

        }
    }


}
