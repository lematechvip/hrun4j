package vip.lematech.hrun4j.core.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.ApiModel;
import vip.lematech.hrun4j.entity.testcase.Config;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import vip.lematech.hrun4j.entity.testcase.TestStep;
import vip.lematech.hrun4j.helper.LogHelper;
import org.testng.collections.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * api to testcase converter
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class ObjectConverter {

    /**
     * api to testcase
     *
     * @param apiModel Interface definition entity
     * @return Test case object
     */
    public static TestCase apiModel2TestCase(ApiModel apiModel) {
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
     * @param apiModel Interface definition entity
     * @return Test step object
     */
    public static TestStep apiModel2TestStep(ApiModel apiModel) {
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
     * @param baseObj Extended object
     * @param extendedObj Base object
     * @return The object after processing
     */
    public static Object objectsExtendsPropertyValue(Object baseObj,Object extendedObj) {
        if(Objects.isNull(baseObj) || Objects.isNull(extendedObj)){
            throw new DefinedException("The source and target objects cannot be null");
        }
        if(baseObj.getClass() != extendedObj.getClass()){
            throw new DefinedException("The source object and the target object must belong to the same class");
        }
        Field[] fields = getObjectAllFields(extendedObj);
        int fieldLength = fields.length;
        for (int index = 0; index < fieldLength; index++) {
            String attributeName = fields[index].getName();
            Class attributeClass = fields[index].getType();
            String methodName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);

            if (fields[index].isSynthetic()) {
                continue;
            }

            Object fieldValue = getFieldValueByName(fields[index].getName(), extendedObj);
            if (Objects.isNull(fieldValue)) {
                continue;
            }
            Field[] subFields = getObjectAllFields(baseObj);
            int subFieldLength = subFields.length;
            for (int subIndex = 0; subIndex < subFieldLength; subIndex++) {
                String subAttributeName = subFields[subIndex].getName();
                Class subAttributeClass = subFields[subIndex].getType();
                String subMethodName = subAttributeName.substring(0, 1).toUpperCase() + subAttributeName.substring(1);
                if (methodName.equals(subMethodName) && subAttributeClass == attributeClass) {
                    Object subFieldValue = getFieldValueByName(subFields[subIndex].getName(), baseObj);
                    LogHelper.debug("Parent Type：{},Parent Value：{},Parent Method Name：{},Child Type：{},Child Value：{},Child Method Name：{}", attributeClass, fieldValue, methodName, subAttributeClass, subFieldValue, subMethodName);
                    if (Objects.isNull(subFieldValue)) {
                        subFieldValueIsNullAssignment(baseObj, subAttributeClass, methodName, fieldValue);
                    } else {
                        subFieldValueIsNotNullAssignment(baseObj, subAttributeClass, methodName, fieldValue, subFieldValue);
                    }
                    break;
                }
            }
        }
        return baseObj;
    }


    /**
     * Assigns a value to an attribute whose child object is null
     *
     * @param baseObj
     * @param subAttributeClass
     * @param methodName
     * @param fieldValue
     */
    private static void subFieldValueIsNotNullAssignment(Object baseObj, Class subAttributeClass, String methodName, Object fieldValue, Object subFieldValue) {
        try {
            if (subAttributeClass == Map.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Map.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == String.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, String.class);
                setMethod.invoke(baseObj, String.valueOf(fieldValue));
            }else if (subAttributeClass == List.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, List.class);
                setMethod.invoke(baseObj, subFieldValue);
            } else if (subAttributeClass == Integer.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Integer.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == Double.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Double.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == JSONObject.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, JSONObject.class);
                setMethod.invoke(baseObj, subFieldValue);
            } else if (subAttributeClass == Boolean.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Boolean.class);
                setMethod.invoke(baseObj, fieldValue);
            }else if (subAttributeClass == RequestEntity.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, RequestEntity.class);
                RequestEntity targetRequestEntity = (RequestEntity) objectsExtendsPropertyValue(subFieldValue, fieldValue);
                setMethod.invoke(baseObj, targetRequestEntity);
            } else if (subAttributeClass == Object.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Object.class);
                setMethod.invoke(baseObj, fieldValue);
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
     * @param baseObj
     * @param subAttributeClass
     * @param methodName
     * @param fieldValue
     */
    private static void subFieldValueIsNullAssignment(Object baseObj, Class subAttributeClass, String methodName, Object fieldValue) {
        try {
            if (subAttributeClass == String.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, String.class);
                setMethod.invoke(baseObj, String.valueOf(fieldValue));
            } else if (subAttributeClass == JSONObject.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, JSONObject.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == List.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, List.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == Map.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Map.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == Integer.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Integer.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == Double.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Double.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == Boolean.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Boolean.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == RequestEntity.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, RequestEntity.class);
                setMethod.invoke(baseObj, fieldValue);
            } else if (subAttributeClass == Object.class) {
                Method setMethod = baseObj.getClass().getMethod("set" + methodName, Object.class);
                setMethod.invoke(baseObj, fieldValue);
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
    private static Field[] getObjectAllFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        List<Field> fieldList = CollUtil.newArrayList(fields);
        fieldList.addAll(CollUtil.newArrayList(superFields));
        return Convert.convert(Field[].class, fieldList);
    }


    /**
     * get object field value by name
     *
     * @param fieldName field of name
     * @param obj       object
     * @return Gets the value of the object's specified property
     */
    public static Object getFieldValueByName(String fieldName, Object obj) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(obj, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            String exceptionMsg = String.format("Abnormal reflection, abnormal message: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }

    /**
     * @param sourceMap The source mapping relationship
     * @param targetMap The target mapping relationship
     * @return The mapping relationship after superposition
     */
    public static Map<String, Object> mapExtendsKeyValue(Map sourceMap, Map targetMap) {
        if (MapUtil.isEmpty(targetMap)) {
            targetMap = Maps.newHashMap();
        }
        targetMap.putAll(MapUtil.isEmpty(sourceMap) ? Maps.newHashMap() : sourceMap);
        return targetMap;
    }

}
