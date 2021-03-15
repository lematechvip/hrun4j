package io.lematech.httprunner4j;

import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.utils.ExpressionProcessor;
import io.lematech.httprunner4j.utils.HttpClientUtil;
import io.lematech.httprunner4j.utils.RegExpUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.util.*;

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
        for(int index = 0;index <testSteps.size();index++) {
            TestStep testStep = testSteps.get(index);
            log.info("STEP[{}] : {}", (index + 1), testStep.getName());
            expressionProcessor.setVariablePriority(testContextVariable, config.getVariables(), testStep.getVariables());
            RequestEntity requestEntity = (RequestEntity) expressionProcessor.executeExpression(testStep.getRequest());
            requestEntity.setUrl(getUrl(config.getBaseUrl(), testStep.getRequest().getUrl()));
            ResponseEntity responseEntity = HttpClientUtil.executeReq(requestEntity);
            List<Map<String, Object>> validateList = testStep.getValidate();
            AssertChecker.assertList(validateList, responseEntity);
            extractsVariables(testStep.getExtract(), responseEntity);
        }
    }

    private String getUrl(String baseUrl, String requestUrl) {
        if (RegExpUtil.isUrl(requestUrl)) {
            return requestUrl;
        }
        return String.format("%s%s", baseUrl, requestUrl);
    }

    private void extractsVariables(Object extracts, ResponseEntity responseEntity) {
        if (Objects.isNull(extracts)) {
            return;
        }
        log.debug("extracts 类型：{}", extracts.getClass());
        Class clz = extracts.getClass();
        if (clz == ArrayList.class) {
            List<Map<String, String>> extractList = (List<Map<String, String>>) extracts;
            for (Map extractMap : extractList) {
                Iterator<Map.Entry<String, String>> entries = extractMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String transferValue = AssertChecker.dataTransfer(value, responseEntity);
                    testContextVariable.put(key, transferValue);
                }
            }
        } else if (clz == Map.class) {
            log.info("Map类型");
        }else{
            log.error("暂不支持此种类型");
        }
    }
}
