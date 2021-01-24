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
            String url = String.format("%s%s",config.getBaseUrl(),testStep.getApi());
            log.info("请求地址：{}",url);
            PooledHttpClientUtil.getHttpClientInstance().doGet(url);
        }
    }


}
