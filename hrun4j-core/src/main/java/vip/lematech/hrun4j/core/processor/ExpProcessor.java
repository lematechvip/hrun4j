package vip.lematech.hrun4j.core.processor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.core.converter.ObjectConverter;
import vip.lematech.hrun4j.entity.base.BaseModel;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.Config;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.helper.RegExpHelper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Slf4j
public class ExpProcessor<T> {
    private Map<String, Object> currentVariable = new HashMap<>();
    private Map<String, Object> configVars = new HashMap<>();
    private Map<String, Object> testStepVars = new HashMap<>();

    public ExpProcessor() {
    }

    /**
     * Dynamically handle the object that contains the value of the expression
     *
     * @param t   The generic object
     * @param <T> The generic type
     * @return The generic object
     */
    public <T> T dynHandleContainsExpObject(T t) {
        if (Objects.isNull(t)) {
            String exceptionMsg = String.format("Expression object cannot be null");
            throw new DefinedException(exceptionMsg);
        }
        if (t instanceof Map) {
            Map<String, Object> instance = (Map) t;
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, Object> entry : instance.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                if (!RegExpHelper.isExp(value)) {
                    result.put(key, value);
                    continue;
                }
                Object handledValue = handleStringExp(value);
                result.put(key, handledValue);
                LogHelper.debug("Expression before handle: {}, after handle: {},Current environment variable: {}", value, handledValue, currentVariable);
            }
            return (T)result;
        }else if (t instanceof String) {
            String str = (String) t;
            return (T) handleStringExp(str);
        } else if (t instanceof RequestEntity || t instanceof Config) {
            return (T) handleBuiltInObject(t);
        }
        return t;
    }


    /**
     * execute string expression
     * @param exp The expression to
     * @param environment  The environment
     * @return result of the expression
     */
    public Object handleStringExp(String exp, Map<String, Object> environment) {
        this.currentVariable.putAll(environment);
        return handleStringExp(exp);
    }

    /**
     * execute string expression
     * @param exp The expression
     * @return result of the expression
     */
    public Object handleStringExp(String exp) {
        if (RegExpHelper.isExp(exp)) {
            String handleExp = new String(exp.getBytes());
            try {
                List<String> matchList = RegExpHelper.find(Constant.REGEX_EXPRESSION, exp);
                String matchExp = matchList.get(0);
                if (matchList.size() == 1) {
                    String onlyExp = String.format("${%s}", matchExp);
                    if (exp.equals(onlyExp)) {
                        return BuiltInAviatorEvaluator.execute(matchExp, currentVariable);
                    } else {
                        exp = getExpString(exp, matchList);
                    }
                } else {
                    exp = getExpString(exp, matchList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String exceptionMsg = String.format("Handle expression %s handles exception, exception information: %s", exp, e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
            LogHelper.debug("Expression before handle: {}, after handle: {},Current environment variable: {}", handleExp, exp, currentVariable);
        }
        return exp;
    }

    private String getExpString(String exp, List<String> matchList) {
        List<String> matchedList = new ArrayList<>();
        for (String subExp : matchList) {
            Object result = BuiltInAviatorEvaluator.execute(subExp, currentVariable);
            String handleResult = String.valueOf(result);
            matchedList.add(handleResult);
        }
        for (String matched : matchedList) {
            exp = exp.replaceFirst(Constant.REGEX_EXPRESSION_REPLACE, matched);
        }
        return exp;
    }

    /**
     * handle variables(config variable、teststep variale) expression in test context variable
     *
     * @param configVars
     * @param testStepVars
     */
    private void handleVariablesExpression(Map<String, Object> configVars, Map<String, Object> testStepVars) {
        this.configVars = (Map<String, Object>) dynHandleContainsExpObject((T) configVars);
        this.testStepVars = (Map<String, Object>) dynHandleContainsExpObject((T) testStepVars);
    }

    /**
     * handle setup or teardwon hook expression
     */
    /**
     * handle setup or teardwon hook expression
     *
     * @param hookObj hook expression
     * @return handle expression results
     */
    public Map handleHookExp(Object hookObj) {
        Map result = Maps.newHashMap();
        if (hookObj instanceof Map) {
            result.putAll((Map) dynHandleContainsExpObject(hookObj));
        } else if (hookObj instanceof List) {
            List tempList = (List) hookObj;
            for (Object obj : tempList) {
                if (obj instanceof String) {
                    this.handleStringExp(String.valueOf(obj));
                } else if (obj instanceof Map) {
                    result.putAll((Map) dynHandleContainsExpObject(obj));
                }
            }
        } else if (hookObj instanceof String) {
            this.handleStringExp(String.valueOf(hookObj));
        }
        return result;
    }

    /**
     * Handle variable priority
     * @param testContextVariable The test context variable
     * @param configVars The configuration variables
     * @param testStepVars The test step variables
     * @param currentVariable The current  variables
     */
    public void setVariablePriority(Map<String, Object> currentVariable, Map<String, Object> testContextVariable, Map<String, Object> configVars, Map<String, Object> testStepVars) {
        handleVariablesExpression(configVars, testStepVars);
        currentVariable.putAll(MapUtil.isEmpty(configVars) ? Maps.newHashMap() : this.configVars);
        currentVariable.putAll(testContextVariable);
        currentVariable.putAll(MapUtil.isEmpty(testStepVars) ? Maps.newHashMap() : this.testStepVars);
        this.currentVariable = currentVariable;
    }

    /**
     * Handle built-in object property values containing expression fields
     *
     * @param object
     * @return
     */
    private Object handleBuiltInObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        int fieldLength = fields.length;
        for (int index = 0; index < fieldLength; index++) {
            String attributeName = fields[index].getName();
            Class attributeClass = fields[index].getType();
            fields[index].setAccessible(true);
            String methodName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            try {
                Object fieldValue = ObjectConverter.getFieldValueByName(fields[index].getName(), object);
                if (StrUtil.isEmptyIfStr(fieldValue)) {
                    continue;
                }
                if (attributeClass == String.class) {
                    Method setMethod = object.getClass().getMethod("set" + methodName, String.class);
                    setMethod.invoke(object, dynHandleContainsExpObject((T) fieldValue));
                } else if (attributeClass == Map.class) {
                    Method setMethod = object.getClass().getMethod("set" + methodName, Map.class);
                    setMethod.invoke(object, dynHandleContainsExpObject((T) fieldValue));
                } else if (attributeClass == JSONObject.class) {
                    Method setMethod = object.getClass().getMethod("set" + methodName, JSONObject.class);
                    JSONObject jsonObject = JSON.parseObject((String) dynHandleContainsExpObject((T) JSON.toJSONString(fieldValue)), JSONObject.class);
                    setMethod.invoke(object, jsonObject);
                } else if (object.getClass() == RequestEntity.class && methodName.equals("Json")) {
                    Method setMethod = object.getClass().getMethod("set" + methodName, Object.class);
                    JSONObject jsonObject = JSON.parseObject((String) dynHandleContainsExpObject((T) JSON.toJSONString(fieldValue)), JSONObject.class);
                    setMethod.invoke(object, jsonObject);
                }else {
                    log.debug("Current Type {} Data Not Processed", attributeClass);
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
                String exceptionMsg = String.format("Handle built-in object property expression exceptions, exception information: %s", e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
        return object;
    }


    /**
     * Converts data types to key-value pairs ,for variable
     * @param baseModel The base model
     */
    public void handleVariables2Map(BaseModel baseModel) {
        Object obj = baseModel.getVariables();
        if (obj instanceof Map) {
            return;
        }
        Map result = Maps.newHashMap();
        if (obj instanceof List) {
            List tempList = (List) obj;
            for (Object elementObj : tempList) {
                if (elementObj instanceof String) {
                    Object executeResult = BuiltInAviatorEvaluator.execute(String.valueOf(obj), this.currentVariable);
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



}
