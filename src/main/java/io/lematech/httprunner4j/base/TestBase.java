package io.lematech.httprunner4j.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TestBase {
    private TestCaseImpl testCaseImpl = new TestCaseImpl();
    private String testCaseName;
    @BeforeSuite
    public void beforeSuite(){
        log.info("[========================================]@beforeSuite()");
    }

    @BeforeMethod
    public void setUp() {
        log.info("[===================="+this.testCaseName+"====================]@START");
    }
    @AfterMethod
    public void tearDown() {
        log.info("[===================="+this.testCaseName+"====================]@END");
    }
    @AfterSuite
    public void afterSuite(){
        log.info("[========================================]@afterSuite()");
    }
    @DataProvider
    public Object[][] dataProvider(Method method){
        this.testCaseName = method.getName();
        TestCase testCase = testCaseImpl.getTestCase(testCaseName);
        List<TestCase> result = handleMultiGroupData(testCase);
        Object[][] testCases = new Object[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            testCases[i] = new Object[] { result.get(i) };
        }
        return testCases;
    }

    /**
     *      user_id: [1001, 1002, 1003, 1004]
     *      username-password:
     *       - ["user1", "111111"]
     *       - ["user2", "222222"]
     *       - ["user3", "333333"]
     */
    private List<TestCase> handleMultiGroupData(TestCase testCase){
        ArrayList<TestCase> result = new ArrayList<>();
        Object parameters = testCase.getConfig().getParameters();
        if(parameters == null){
            result.add(testCase);
            return result;
        }
        if(parameters instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject)parameters;
            for (Map.Entry entry : jsonObject.entrySet()) {
                String key = (String)entry.getKey();
                String []params = key.split("-");
                Object value = entry.getValue();
                if(value instanceof JSONArray){
                    JSONArray array = (JSONArray)value;
                    for(int i=0; i < array.size(); i++){
                        Object arr = array.get(i);
                        ObjectMapper objectMapper = new ObjectMapper();
                        TestCase cpTestCase;
                        try {
                            cpTestCase = objectMapper.readValue(objectMapper.writeValueAsString(testCase), TestCase.class);
                        } catch (JsonProcessingException e) {
                            String exceptionMsg = String.format("testcase deep copy exception : %s",e.getMessage());
                            log.error(exceptionMsg);
                            throw new DefinedException(exceptionMsg);
                        }
                        Map<String,Object> variables = cpTestCase.getConfig().getVariables();
                        Map map = new HashMap<>();
                        Map parameterPro = new HashMap<>();
                        if(params.length == 1){
                            String name = params[0];
                            map.put(name,arr);
                        }else{
                            if(arr instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) arr;
                                int size = jsonArray.size();
                                for (int index = 0; index < size; index++) {
                                    String name = params[index];
                                    map.put(name, jsonArray.get(index));
                                }
                            }
                        }
                        parameterPro.putAll(map);
                        parameterPro.putAll(variables);
                        Config config = cpTestCase.getConfig();
                        config.setVariables(parameterPro);
                        config.setParameters(map);
                        cpTestCase.setConfig(config);
                        result.add(cpTestCase);
                    }
                }
            }
        }
        return result;
    }
}
