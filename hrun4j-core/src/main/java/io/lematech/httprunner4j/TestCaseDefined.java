package io.lematech.httprunner4j;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.utils.ExpressionProcessor;
import io.lematech.httprunner4j.utils.HttpClientUtil;
import io.lematech.httprunner4j.utils.RegExpUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
            TestStep testStep = extendApiModelPropertiesValue(testSteps.get(index));
            log.info("STEP[{}] : {}", (index + 1), testStep.getName());
            expressionProcessor.setVariablePriority(testContextVariable, (Map) config.getVariables(), (Map) testStep.getVariables());
            RequestEntity requestEntity = (RequestEntity) expressionProcessor.executeExpression(testStep.getRequest());
            requestEntity.setUrl(getUrl(config.getBaseUrl(), testStep.getRequest().getUrl()));
            ResponseEntity responseEntity = HttpClientUtil.executeReq(requestEntity);
            List<Map<String, Object>> validateList = testStep.getValidate();
            AssertChecker.assertList(validateList, responseEntity);
            extractsVariables(testStep.getExtract(), responseEntity);
        }
    }

    /**
     * extend api model properties value
     *
     * @param testStep
     * @return
     */
    private TestStep extendApiModelPropertiesValue(TestStep testStep) {
        String api = testStep.getApi();
        if (StrUtil.isEmpty(api)) {
            return testStep;
        }
        // TODO: 2021/3/16  testStep 和 apimodel结构不一致
        String dataFileResourcePath = NGDataProvider.seekModelFileByCasePath(api);
        ApiModel apiModel = TestCaseLoaderFactory.getLoader(FileUtil.extName(api)).load(dataFileResourcePath, RunnerConfig.getInstance().getTestCaseExtName(), ApiModel.class);
        TestStep trsTestStep = (TestStep) objectsExtendsPropertyValue(testStep, apiModel2TestStep(apiModel));
        log.info("Api：{},TS：{},RS：{}", JSON.toJSONString(apiModel), JSON.toJSONString(testStep), JSON.toJSONString(trsTestStep));
        return trsTestStep;
    }

    private TestStep apiModel2TestStep(ApiModel apiModel) {
        TestStep sourceTestStep = new TestStep();
        BeanUtil.copyProperties(apiModel, sourceTestStep);
        RequestEntity requestEntity = sourceTestStep.getRequest();
        requestEntity.setUrl(apiModel.getBaseUrl());
        sourceTestStep.setRequest(requestEntity);
        return sourceTestStep;
    }

    /**
     * objects extends property values
     *
     * @param targetObj
     * @param sourceObj
     * @return
     */
    private Object objectsExtendsPropertyValue(Object targetObj, Object sourceObj) {
        Field[] fields = sourceObj.getClass().getDeclaredFields();
        int fieldLength = fields.length;
        for (int index = 0; index < fieldLength; index++) {
            String attributeName = fields[index].getName();
            Class attributeClass = fields[index].getType();
            fields[index].setAccessible(true);
            String methodName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            try {
                Object fieldValue = validateDataValid(getFieldValueByName(fields[index].getName(), sourceObj));
                if (fieldValue == null) {
                    continue;
                }
                Field[] subFields = targetObj.getClass().getDeclaredFields();
                int subFieldLength = subFields.length;
                for (int subIndex = 0; subIndex < subFieldLength; subIndex++) {
                    String subAttributeName = subFields[subIndex].getName();
                    Class subAttributeClass = subFields[subIndex].getType();
                    String subMethodName = subAttributeName.substring(0, 1).toUpperCase() + subAttributeName.substring(1);
                    if (methodName.equals(subMethodName) && subAttributeClass == attributeClass) {
                        subFields[subIndex].setAccessible(true);
                        Object subFieldValue = validateDataValid(getFieldValueByName(subFields[subIndex].getName(), targetObj));
                        log.info("父类型：{},父值：{},父方法名：{},子类型：{},子值：{},子方法名：{}", attributeClass, fieldValue, methodName, subAttributeClass, subFieldValue, subMethodName);
                        if (Objects.isNull(subFieldValue)) {
                            if (subAttributeClass == String.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, String.class);
                                setMethod.invoke(targetObj, String.valueOf(fieldValue));
                            } else if (subAttributeClass == Object.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == JSONObject.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, JSONObject.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == List.class) {
                                // TODO: 2021/3/17 增量更新
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, List.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == Map.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, Map.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == Boolean.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, Boolean.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == RequestEntity.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, RequestEntity.class);
                                setMethod.invoke(targetObj, fieldValue);
                            }
                        } else {
                            if (subAttributeClass == Map.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, Map.class);
                                ((Map) fieldValue).putAll(MapUtil.isEmpty((Map) subFieldValue) ? Maps.newHashMap() : (Map) subFieldValue);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == List.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, List.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == RequestEntity.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, RequestEntity.class);
                                RequestEntity subRequestEntity = (RequestEntity) subFieldValue;
                                RequestEntity requestEntity = (RequestEntity) fieldValue;
                                setMethod.invoke(targetObj, objectsExtendsPropertyValue(subRequestEntity, requestEntity));
                            }
                        }
                        break;
                    }
                }
            } catch (NoSuchMethodException e) {
                String exceptionMsg = String.format("no such method exception %s", e.getMessage());
                throw new DefinedException(exceptionMsg);
            } catch (IllegalAccessException e) {
                String exceptionMsg = String.format("illegal access exception %s", e.getMessage());
                throw new DefinedException(exceptionMsg);
            } catch (InvocationTargetException e) {
                String exceptionMsg = String.format("invocation target exception %s", e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
        return targetObj;
    }

    private Object validateDataValid(Object fieldValueByName) {
        Object fieldValue = fieldValueByName;
        if (fieldValue == null) {
            return null;
        }
        return fieldValue;
    }

    /**
     * get object field value by name
     *
     * @param fieldName
     * @param o
     * @return
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
