package io.lematech.httprunner4j.core.runner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.loader.Searcher;
import io.lematech.httprunner4j.core.loader.TestDataLoaderFactory;
import io.lematech.httprunner4j.core.validator.AssertChecker;
import io.lematech.httprunner4j.entity.base.BaseModel;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.widget.exp.BuiltInAviatorEvaluator;
import io.lematech.httprunner4j.widget.exp.ExpHandler;
import io.lematech.httprunner4j.widget.utils.HttpClientUtil;
import io.lematech.httprunner4j.widget.utils.RegExpUtil;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.collections.Maps;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestCaseDefined
 * @description TODO
 * @created 2021/1/20 11:07 上午
 * @publicWechat lematech
 */
public class TestCaseRunner {
    private ExpHandler expHandler;
    /**
     * testcase context variables
     */
    private Map<String, Object> testContextVariable;

    /**
     * teststep context variables
     */
    private Map<String, Object> testStepConfigVariable;

    private Searcher searcher;

    /**
     * assert checker
     */
    private AssertChecker assertChecker;

    public TestCaseRunner() {
        expHandler = new ExpHandler();
        testContextVariable = Maps.newHashMap();
        searcher = new Searcher();
        assertChecker = new AssertChecker(expHandler);
    }

    /**
     * real execute testcase logic
     */
    public void execute(TestCase testCase) {
        List<TestStep> testSteps = testCase.getTestSteps();
        Config config = (Config) expHandler.executeExpression(testCase.getConfig());
        setupHook(config);
        for (int index = 0; index < testSteps.size(); index++) {
            testStepConfigVariable = Maps.newHashMap();
            MyLog.info("步骤 : {}", testSteps.get(index).getName());
            Map configVariables = Objects.isNull(config) ? Maps.newHashMap() : (Map) config.getVariables();
            TestStep testStep = referenceApiModelOrTestCase(testSteps.get(index), configVariables);
            RequestEntity initializeRequestEntity = testStep.getRequest();
            if (Objects.isNull(initializeRequestEntity)) {
                continue;
            }
            testStepConfigVariable.put("request", initializeRequestEntity);
            setupHook(testStep);
            expHandler.setVariablePriority(testStepConfigVariable, testContextVariable, configVariables, (Map) testStep.getVariables());
            RequestEntity requestEntity = (RequestEntity) expHandler.executeExpression(initializeRequestEntity);
            requestEntity.setUrl(getUrl(config.getBaseUrl(), testStep.getRequest().getUrl()));
            ResponseEntity responseEntity = HttpClientUtil.executeReq(requestEntity);
            List<Map<String, Object>> validateList = testStep.getValidate();
            testStepConfigVariable.put("response", responseEntity);
            teardownHook(testStep);
            assertChecker.assertList(validateList, responseEntity, this.testStepConfigVariable);
            extractVariables(testStep.getExtract(), responseEntity);
        }
        teardownHook(config);
    }

    private void setupHook(Object obj) {
        hook(obj, "setup");
    }

    private void hook(Object obj, String type) {
        if (Objects.isNull(obj)) {
            return;
        }
        Map result;
        handleVariables2Map((BaseModel) obj);
        if (obj instanceof Config) {
            Config transConfig = (Config) obj;
            Object hookObj = transConfig.getSetupHooks();
            if (Objects.isNull(hookObj)) {
                return;
            }
            if ("teardown".equals(type)) {
                hookObj = transConfig.getTeardownHooks();
            }
            MyLog.info("执行配置{}方法集：", type);
            result = expHandler.handleHookExp(hookObj);
            Map variablesMap = Maps.newHashMap();
            variablesMap.putAll(MapUtil.isEmpty((Map) transConfig.getVariables()) ? Maps.newHashMap() : (Map) transConfig.getVariables());
            variablesMap.putAll(MapUtil.isEmpty(result) ? Maps.newHashMap() : result);
            ((Config) obj).setVariables(variablesMap);
        } else if (obj instanceof TestStep) {
            TestStep transTestStep = (TestStep) obj;
            Object hookObj = transTestStep.getSetupHooks();
            if ("teardown".equals(type)) {
                hookObj = transTestStep.getTeardownHooks();
                outputVariables(transTestStep);
            }
            if (Objects.isNull(hookObj)) {
                return;
            }
            MyLog.info("执行步骤{}方法集：", type);
            result = expHandler.handleHookExp(hookObj);
            Map variablesMap = Maps.newHashMap();
            variablesMap.putAll((Map) transTestStep.getVariables());
            variablesMap.putAll(result);
            ((TestStep) obj).setVariables(variablesMap);
        }
    }

    private void outputVariables(TestStep transTestStep) {
        List outputs = transTestStep.getOutput();
        if (!Objects.isNull(outputs)) {
            if (outputs.contains("variables")) {
                MyLog.info("输出变量[variables]：{}", transTestStep.getVariables());
            }
            if (outputs.contains("extract")) {
                MyLog.info("输出变量[extract]：{}", this.testStepConfigVariable);
            }
        }
    }

    /**
     * variables must transfer to map
     *
     * @param baseModel
     */
    private void handleVariables2Map(BaseModel baseModel) {
        Object obj = baseModel.getVariables();
        if (obj instanceof Map) {
            return;
        }
        Map result = Maps.newHashMap();
        if (obj instanceof List) {
            List tempList = (List) obj;
            for (Object elementObj : tempList) {
                if (elementObj instanceof String) {
                    Object executeResult = BuiltInAviatorEvaluator.execute(String.valueOf(obj), this.testStepConfigVariable);
                    if (executeResult instanceof Map) {
                        result.putAll((Map) executeResult);
                    }
                } else if (elementObj instanceof Map) {
                    result.putAll((Map) elementObj);
                }
            }
        }
        baseModel.setVariables(result);
    }

    private void teardownHook(Object obj) {
        hook(obj, "teardown");
    }

    /**
     * extend api model properties value
     *
     * @param testStep
     * @return
     */
    private TestStep referenceApiModelOrTestCase(TestStep testStep, Map variables) {
        String testcase = testStep.getTestcase();
        if (!StrUtil.isEmpty(testcase)) {
            if (!testcase.startsWith(Constant.TEST_CASE_DIRECTORY_NAME) &&
                    !testcase.startsWith(File.separator + Constant.TEST_CASE_DIRECTORY_NAME)) {
                testcase = Constant.TEST_CASE_DIRECTORY_NAME + File.separator + testcase;
            }
            File testCasePath = searcher.searchDataFileByRelativePath(testcase);
            /**
             * config variables can express to reference testcases
             */
            TestCase testCase = TestDataLoaderFactory.getLoader(FileUtil.extName(testcase)).load(testCasePath, TestCase.class);
            Config tcConfig = testCase.getConfig();
            Map tcVariables = (Map) tcConfig.getVariables();
            if (MapUtil.isEmpty(tcVariables)) {
                tcVariables = Maps.newHashMap();
            }
            tcVariables.putAll(MapUtil.isEmpty(variables) ? Maps.newHashMap() : variables);
            tcConfig.setVariables(tcVariables);
            this.execute(testCase);
        }
        String api = testStep.getApi();
        if (!StrUtil.isEmpty(api)) {
            if (!api.startsWith(Constant.API_DEFINE_DIRECTORY_NAME) &&
                    !api.startsWith(File.separator + Constant.API_DEFINE_DIRECTORY_NAME)) {
                api = Constant.API_DEFINE_DIRECTORY_NAME + File.separator + api;
            }
            File apiFilePath = searcher.searchDataFileByRelativePath(api);
            ApiModel apiModel = TestDataLoaderFactory.getLoader(FileUtil.extName(apiFilePath)).load(apiFilePath, ApiModel.class);
            TestStep trsTestStep = (TestStep) objectsExtendsPropertyValue(testStep, apiModel2TestStep(apiModel));
            MyLog.debug("Api：{},TS：{},RS：{}", JSON.toJSONString(apiModel), JSON.toJSONString(testStep), JSON.toJSONString(trsTestStep));
            return testStep;
        }
        return testStep;
    }

    private TestStep apiModel2TestStep(ApiModel apiModel) {
        TestStep sourceTestStep = new TestStep();
        BeanUtil.copyProperties(apiModel, sourceTestStep);
        RequestEntity requestEntity = sourceTestStep.getRequest();
        requestEntity.setUrl(apiModel.getRequest().getUrl());
        sourceTestStep.setRequest(requestEntity);
        return sourceTestStep;
    }

    private Field[] getObjectAllFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        List<Field> fieldList = CollUtil.newArrayList(fields);
        fieldList.addAll(CollUtil.newArrayList(superFields));
        return Convert.convert(Field[].class, fieldList);
    }

    /**
     * objects extends property values
     *
     * @param targetObj
     * @param sourceObj
     * @return
     */
    private Object objectsExtendsPropertyValue(Object targetObj, Object sourceObj) {
        Field[] fields = getObjectAllFields(sourceObj);
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
                Field[] subFields = getObjectAllFields(targetObj);
                int subFieldLength = subFields.length;
                for (int subIndex = 0; subIndex < subFieldLength; subIndex++) {
                    String subAttributeName = subFields[subIndex].getName();
                    Class subAttributeClass = subFields[subIndex].getType();
                    String subMethodName = subAttributeName.substring(0, 1).toUpperCase() + subAttributeName.substring(1);
                    if (methodName.equals(subMethodName) && subAttributeClass == attributeClass) {
                        subFields[subIndex].setAccessible(true);
                        Object subFieldValue = validateDataValid(getFieldValueByName(subFields[subIndex].getName(), targetObj));
                        MyLog.debug("父类型：{},父值：{},父方法名：{},子类型：{},子值：{},子方法名：{}", attributeClass, fieldValue, methodName, subAttributeClass, subFieldValue, subMethodName);
                        if (Objects.isNull(subFieldValue)) {
                            if (subAttributeClass == String.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, String.class);
                                setMethod.invoke(targetObj, String.valueOf(fieldValue));
                            } else if (subAttributeClass == JSONObject.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, JSONObject.class);
                                setMethod.invoke(targetObj, fieldValue);
                            } else if (subAttributeClass == List.class) {
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
                            } else if (subAttributeClass == Object.class) {
                                if (fieldValue instanceof Map) {
                                    Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                                    setMethod.invoke(targetObj, fieldValue);
                                } else if (fieldValue instanceof List) {
                                    Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                                    setMethod.invoke(targetObj, fieldValue);
                                }
                            }
                        } else {
                            if (subAttributeClass == Map.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, Map.class);
                                Map resultMap = Maps.newHashMap();
                                resultMap.putAll(MapUtil.isEmpty((Map) fieldValue) ? Maps.newHashMap() : (Map) fieldValue);
                                resultMap.putAll(((Map) subFieldValue));
                                setMethod.invoke(targetObj, resultMap);
                            } else if (subAttributeClass == List.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, List.class);
                                ((List) subFieldValue).addAll((List) fieldValue);
                                setMethod.invoke(targetObj, subFieldValue);
                            } else if (subAttributeClass == JSONObject.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, JSONObject.class);
                                setMethod.invoke(targetObj, subFieldValue);
                            } else if (subAttributeClass == RequestEntity.class) {
                                Method setMethod = targetObj.getClass().getMethod("set" + methodName, RequestEntity.class);
                                RequestEntity subRequestEntity = (RequestEntity) subFieldValue;
                                RequestEntity requestEntity = (RequestEntity) fieldValue;
                                RequestEntity targetRequestEntity = (RequestEntity) objectsExtendsPropertyValue(subRequestEntity, requestEntity);
                                setMethod.invoke(targetObj, targetRequestEntity);
                            } else if (subAttributeClass == Object.class) {
                                if (subFieldValue instanceof Map && fieldValue instanceof Map) {
                                    Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                                    ((Map) subFieldValue).putAll(MapUtil.isEmpty((Map) fieldValue) ? Maps.newHashMap() : (Map) fieldValue);
                                    setMethod.invoke(targetObj, subFieldValue);
                                } else if (subFieldValue instanceof List && fieldValue instanceof List) {
                                    Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                                    ((List) subFieldValue).addAll((List) fieldValue);
                                    setMethod.invoke(targetObj, subFieldValue);
                                }
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

    private void extractVariables(Object extracts, ResponseEntity responseEntity) {
        if (Objects.isNull(extracts)) {
            return;
        }
        MyLog.debug("extracts 类型：{}", extracts.getClass());
        Class clz = extracts.getClass();
        if (clz == ArrayList.class) {
            List<Map<String, String>> extractList = (List<Map<String, String>>) extracts;
            for (Map extractMap : extractList) {
                extractMap(responseEntity, extractMap);
            }
        } else if (clz == Map.class || clz == LinkedHashMap.class) {
            Map extractMap = (Map) extracts;
            extractMap(responseEntity, extractMap);
        } else {
            MyLog.error("暂不支持此种类型{}提取数据", extracts);
        }
    }

    private void extractMap(ResponseEntity responseEntity, Map extractMap) {
        Iterator<Map.Entry<String, String>> entries = extractMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String key = entry.getKey();
            String value = entry.getValue();
            Object transferValue = assertChecker.dataTransfer(value, responseEntity, this.testStepConfigVariable);
            if (transferValue == value) {
                String exceptionMsg = String.format("not found value by given search model : %s", value);
                throw new DefinedException(exceptionMsg);
            }
            testContextVariable.put(key, transferValue);
        }
    }
}
