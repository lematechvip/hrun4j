package io.lematech.httprunner4j.core.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.collections.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ObjConverter
 * @description api to testcase converter
 * @created 2021/4/6 5:55 下午
 * @publicWechat lematech
 */
public class ObjectConverter {

    /**
     * api to testcase
     *
     * @param apiModel
     * @return
     */
    public TestCase apiModel2TestCase(ApiModel apiModel) {
        TestCase testCase = new TestCase();
        TestStep testStep = new TestStep();
        Config config = new Config<>();
        config.setBaseUrl(apiModel.getBaseUrl());
        BeanUtil.copyProperties(apiModel, testStep);
        List<TestStep> testSteps = new ArrayList<>();
        testSteps.add(testStep);
        testCase.setConfig(config);
        testCase.setTestSteps(testSteps);
        return testCase;
    }

    /**
     * api to teststep
     *
     * @param apiModel
     * @return
     */
    public TestStep apiModel2TestStep(ApiModel apiModel) {
        TestStep sourceTestStep = new TestStep();
        BeanUtil.copyProperties(apiModel, sourceTestStep);
        RequestEntity requestEntity = sourceTestStep.getRequest();
        requestEntity.setUrl(apiModel.getRequest().getUrl());
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
    public Object objectsExtendsPropertyValue(Object targetObj, Object sourceObj) {
        Field[] fields = getObjectAllFields(sourceObj);
        int fieldLength = fields.length;
        for (int index = 0; index < fieldLength; index++) {
            String attributeName = fields[index].getName();
            Class attributeClass = fields[index].getType();
            String methodName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Object fieldValue = getFieldValueByName(fields[index].getName(), sourceObj);
            if (Objects.isNull(fieldValue)) {
                continue;
            }
            Field[] subFields = getObjectAllFields(targetObj);
            int subFieldLength = subFields.length;
            for (int subIndex = 0; subIndex < subFieldLength; subIndex++) {
                String subAttributeName = subFields[subIndex].getName();
                Class subAttributeClass = subFields[subIndex].getType();
                String subMethodName = subAttributeName.substring(0, 1).toUpperCase() + subAttributeName.substring(1);
                if (methodName.equals(subMethodName) && subAttributeClass == attributeClass) {
                    Object subFieldValue = getFieldValueByName(subFields[subIndex].getName(), targetObj);
                    MyLog.debug("Parent Type：{},Parent Value：{},Parent Method Name：{},Child Type：{},Child Value：{},Child Method Name：{}", attributeClass, fieldValue, methodName, subAttributeClass, subFieldValue, subMethodName);
                    if (Objects.isNull(subFieldValue)) {
                        subFieldValueIsNullAssignment(targetObj, subAttributeClass, methodName, fieldValue);
                    } else {
                        subFieldValueIsNotNullAssignment(targetObj, subAttributeClass, methodName, fieldValue, subFieldValue);
                    }
                    break;
                }
            }
        }
        return targetObj;
    }


    /**
     * Assigns a value to an attribute whose child object is null
     *
     * @param targetObj
     * @param subAttributeClass
     * @param methodName
     * @param fieldValue
     */
    private void subFieldValueIsNotNullAssignment(Object targetObj, Class subAttributeClass, String methodName, Object fieldValue, Object subFieldValue) {
        try {
            if (subAttributeClass == Map.class) {
                Method setMethod = targetObj.getClass().getMethod("set" + methodName, Map.class);
                setMethod.invoke(targetObj, mapExtendsKeyValue((Map) fieldValue, (Map) subFieldValue));
            } else if (subAttributeClass == List.class) {
                Method setMethod = targetObj.getClass().getMethod("set" + methodName, List.class);
                ((List) subFieldValue).addAll((List) fieldValue);
                setMethod.invoke(targetObj, subFieldValue);
            } else if (subAttributeClass == JSONObject.class) {
                Method setMethod = targetObj.getClass().getMethod("set" + methodName, JSONObject.class);
                setMethod.invoke(targetObj, subFieldValue);
            } else if (subAttributeClass == RequestEntity.class) {
                Method setMethod = targetObj.getClass().getMethod("set" + methodName, RequestEntity.class);
                RequestEntity targetRequestEntity = (RequestEntity) objectsExtendsPropertyValue(subFieldValue, fieldValue);
                setMethod.invoke(targetObj, targetRequestEntity);
            } else if (subAttributeClass == Object.class) {
                if (subFieldValue instanceof Map && fieldValue instanceof Map) {
                    Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                    setMethod.invoke(targetObj, mapExtendsKeyValue((Map) fieldValue, (Map) subFieldValue));
                } else if (subFieldValue instanceof List && fieldValue instanceof List) {
                    Method setMethod = targetObj.getClass().getMethod("set" + methodName, Object.class);
                    ((List) subFieldValue).addAll((List) fieldValue);
                    setMethod.invoke(targetObj, subFieldValue);
                }
            }
        } catch (NoSuchMethodException e) {
            String exceptionMsg = String.format("No such method exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (IllegalAccessException e) {
            String exceptionMsg = String.format("Illegal access exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (InvocationTargetException e) {
            String exceptionMsg = String.format("Invocation target exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (Exception e) {
            String exceptionMsg = String.format("Object property value inheritance exception, exception information:: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }

    }

    /**
     * Assigns a value to an attribute whose child object is null
     *
     * @param targetObj
     * @param subAttributeClass
     * @param methodName
     * @param fieldValue
     */
    private void subFieldValueIsNullAssignment(Object targetObj, Class subAttributeClass, String methodName, Object fieldValue) {
        try {
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
        } catch (NoSuchMethodException e) {
            String exceptionMsg = String.format("No such method exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (IllegalAccessException e) {
            String exceptionMsg = String.format("Illegal access exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (InvocationTargetException e) {
            String exceptionMsg = String.format("Invocation target exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (Exception e) {
            String exceptionMsg = String.format("Object property value inheritance exception, exception information:: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }

    }

    /**
     * get object all fields value
     *
     * @param obj
     * @return
     */
    private Field[] getObjectAllFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        List<Field> fieldList = CollUtil.newArrayList(fields);
        fieldList.addAll(CollUtil.newArrayList(superFields));
        return Convert.convert(Field[].class, fieldList);
    }


    /**
     * get object field value by name
     *
     * @param fieldName
     * @param o
     * @return
     */
    public Object getFieldValueByName(String fieldName, Object o) {
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

    /**
     * @param sourceMap
     * @param targetMap
     * @return
     */
    public Map<String, Object> mapExtendsKeyValue(Map sourceMap, Map targetMap) {
        if (MapUtil.isEmpty(targetMap)) {
            targetMap = Maps.newHashMap();
        }
        targetMap.putAll(MapUtil.isEmpty(sourceMap) ? Maps.newHashMap() : sourceMap);
        return targetMap;
    }

}
