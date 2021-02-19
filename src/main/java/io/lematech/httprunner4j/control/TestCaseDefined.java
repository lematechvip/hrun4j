package io.lematech.httprunner4j.control;

import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.utils.AssertUtil;
import io.lematech.httprunner4j.utils.ExpressionProcessor;
import io.lematech.httprunner4j.utils.MyHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestCaseDefined
 * @description TODO
 * @created 2021/1/20 11:07 上午
 * @publicWechat lematech
 */
@Slf4j
public class TestCaseDefined {

    private ExpressionProcessor expressionProcessor;
    private TestCase testCase;
    private Map<String,Object> testContextVariable;

    public TestCaseDefined(){
        expressionProcessor = new ExpressionProcessor();
        testContextVariable = Maps.newHashMap();
    }

    /**
     * execute defined testcase
     * @param testCase
     */
    public void execute(TestCase testCase){
        this.testCase = testCase;
        execute();
    }

    /**
     * real execute testcase logic
     */
    private void execute(){
        Config config = testCase.getConfig();
        List<TestStep> testSteps  = testCase.getTestSteps();
        for(int index = 0;index <testSteps.size();index++){
            TestStep testStep = testSteps.get(index);
            log.info("STEP[{}] : {}",index,testStep.getName());
            String url = String.format("%s%s",config.getBaseUrl(),testStep.getRequest().getUrl());
            Map<String,Object> configVars = config.getVariables();
            Map<String,Object> testStepVars = testStep.getVariables();
            RequestEntity requestEntity = testStep.getRequest();
            requestEntity.setUrl(url);
            expressionProcessor.setVariablePriority(testContextVariable,configVars,testStepVars);
            RequestEntity requestNewEntity = (RequestEntity) expressionProcessor.executeExpression(requestEntity);
            log.info(requestNewEntity.toString());
            ResponseEntity responseEntity = MyHttpClient.executeReq(requestNewEntity);
            log.info(responseEntity.toString());
            List<Map<String,Object>> validateList = testStep.getValidate();
            AssertUtil.assertList(validateList,responseEntity);
            extractsVariable(testStep.getExtract(),responseEntity);
        }
    }

    private void extractsVariable(Object extracts,ResponseEntity responseEntity){
        log.info("extracts 类型：{}",extracts.getClass());
        Class clz = extracts.getClass();
        if(clz == ArrayList.class){
            log.info("list类型");
            List<Map<String,String>> extractList = (List<Map<String,String>>)extracts;
            for(Map extractMap : extractList){
                Iterator<Map.Entry<String, String>> entries = extractMap.entrySet().iterator();
                while (entries.hasNext()){
                    Map.Entry<String, String> entry = entries.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String transferValue = AssertUtil.dataTransfer(value,responseEntity);
                    testContextVariable.put(key,transferValue);
                }
            }
        }else if(clz == Map.class){
            log.info("Map类型");
        }else{
            log.error("暂不支持此种类型");
        }
    }
}
