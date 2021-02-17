package io.lematech.httprunner4j.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.utils.Log;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TestBase {
    private TestCaseImpl testCaseImpl = new TestCaseImpl();
    private String testCaseName;
    /**
     * 加载测试驱动
     * @throws DefinedException
     */
    @BeforeSuite
    public void beforeSuite() throws DefinedException {
        System.out.println("beforeSuite");
    }


    /**
     * 输出当前测试用例名称、标记测试用例执行序号
     */
    @BeforeMethod
    public void setUp() {
        Log.comment("----------"+this.testCaseName+"_Case----------start");
        Log.step = 0;
    }

    @AfterMethod
    public void tearDown() {
        Log.comment("----------"+this.testCaseName+"_Case----------end");
    }
    /**
     * 卸载测试驱动
     * @throws DefinedException
     */
    @AfterSuite
    public void afterSuite() throws DefinedException {
        System.out.println("afterSuite");
    }
    /**
     * 初始化获取测试数据对象
     */
    public TestBase() {
       /* try {
            if (!"".equals(Config.getConfig("testData"))){
                td = (ITestDatas) Class.forName(Config.getConfig("testData")).newInstance();
            }else{
                td = new TestData();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
    /**
     * 通过指定或默认方式获取测试数据
     * @param method
     * @return
     */
    @DataProvider
    public Object[][] dataProvider(Method method) throws JsonProcessingException {
        System.out.println("数据驱动");
        this.testCaseName = method.getName();
        TestCase testCase = testCaseImpl.getTestCase(testCaseName);
        Object parameters = testCase.getConfig().getParameters();
        ArrayList<TestCase> result = new ArrayList<>();
        //log.info("数据类型：{}",parameters.getClass());
        if(parameters instanceof JSONObject){
            JSONObject jsonObject = (JSONObject)parameters;
            for (Map.Entry entry : jsonObject.entrySet()) {
                String key = (String)entry.getKey();
                String []params = key.split("-");
                Object value = entry.getValue();
              //  log.info("参数名称:{},参数值列表类型：{}",key,value.getClass());
                if(value instanceof JSONArray){
                    /**
                     *      user_id: [1001, 1002, 1003, 1004]
                     *      username-password:
                     *       - ["user1", "111111"]
                     *       - ["user2", "222222"]
                     *       - ["user3", "333333"]
                     */

                    JSONArray array = (JSONArray)value;
                    for(int i=0; i < array.size(); i++){
                       // ArrayList<Map<String, Object>> result = new ArrayList<>();
                        Object arr = array.get(i);
                        Map map = new HashMap<>();
                        ObjectMapper objectMapper = new ObjectMapper();
                        if(params.length == 1){
                            TestCase cpTestCase = objectMapper.readValue(objectMapper.writeValueAsString(testCase), TestCase.class);
                            Map<String,Object> variables = cpTestCase.getConfig().getVariables();
                     //       log.info("一组参数");
                            String name = params[0];
                            //String indexValue = String.valueOf(arr);
                            map.put(name,arr);
                            Map parameterPro = new HashMap<>();
                            parameterPro.putAll(map);
                            parameterPro.putAll(variables);
                            Config config = cpTestCase.getConfig();
                            config.setVariables(parameterPro);
                            config.setParameters(map);
                            cpTestCase.setConfig(config);
                            result.add(cpTestCase);
                        }else{
                            if(arr instanceof JSONArray) {
                                TestCase cpTestCase = objectMapper.readValue(objectMapper.writeValueAsString(testCase), TestCase.class);
                                Map<String,Object> variables = cpTestCase.getConfig().getVariables();
                                JSONArray jsonArray = (JSONArray) arr;
                          //      log.info("json列表：{}", jsonArray);
                                int size = jsonArray.size();
                                for (int index = 0; index < size; index++) {
                                    String name = params[index];
                             //       log.info("name：{},value：{}", name,jsonArray.get(index));
                                    map.put(name, jsonArray.get(index));
                                }
                                Map parameterPro = new HashMap<>();
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
            }
        }
       // log.info("数据结构：{}", JSON.toJSONString(result));

        Object[][] testDatas = new Object[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            testDatas[i] = new Object[] { result.get(i) };
        }

        /**
         *      user_id: [1001, 1002, 1003, 1004]
         *      username-password:
         *       - ["user1", "111111"]
         *       - ["user2", "222222"]
         *       - ["user3", "333333"]
         */
        return testDatas;
    }
}
