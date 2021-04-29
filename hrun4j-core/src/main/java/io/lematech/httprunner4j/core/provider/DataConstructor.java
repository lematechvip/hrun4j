package io.lematech.httprunner4j.core.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.processor.ExpProcessor;
import io.lematech.httprunner4j.widget.exp.BuiltInAviatorEvaluator;
import io.lematech.httprunner4j.widget.utils.JsonUtil;
import io.lematech.httprunner4j.widget.utils.RegExpUtil;
import jdk.nashorn.internal.runtime.regexp.RegExp;

import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DataConstructor
 * @description Data constructor
 * @created 2021/4/29 2:32 下午
 * @publicWechat lematech
 */
public class DataConstructor {
    private ExpProcessor expProcessor;

    public DataConstructor() {
        expProcessor = new ExpProcessor();
    }

    /**
     * @param paramsObj
     * @return
     */
    public List<Map<String, Object>> parameterized(Object paramsObj) {
        List<Map<String, Object>> parameters = new ArrayList<>();
        if (Objects.isNull(paramsObj)) {
            return parameters;
        }
        String paramsStr = JSON.toJSONString(paramsObj);
        if (!JsonUtil.isJson(paramsStr)) {
            String exceptionMsg = String.format("The parameters data %s json format is incorrect", paramsStr);
            throw new DefinedException(exceptionMsg);
        } else {
            paramsObj = JSONObject.parseObject(paramsStr);
        }
        JSONObject jsonObject = (JSONObject) paramsObj;
        boolean isAssociationParameter = false;
        List<List<Object>> dimValue = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String paramsName = entry.getKey();
            if (StrUtil.isEmpty(paramsName)) {
                String exceptionMsg = String.format("The parameter key name cannot be empty");
                throw new DefinedException(exceptionMsg);
            }
            String[] paramName = paramsName.split(Constant.PARAMETER_SEPARATOR);
            Object parameterValue = entry.getValue();
            if (paramName.length > 1) {
                if (jsonObject.size() != 1) {
                    String exceptionMsg = String.format("The correlation parameter needs to specify the parameter name and the corresponding relationship of the parameter value at one time, and the parameter is divided by '-'");
                    throw new DefinedException(exceptionMsg);
                }
                if (isAssociationParameter) {
                    String exceptionMsg = String.format(" Even if the associated parameter is also a specified parameter is not allowed");
                    throw new DefinedException(exceptionMsg);
                }
                return associationParameterized(paramName, parameterValue);
            } else {
                if (parameterValue instanceof JSONArray) {
                    List<Object> parameterMutiValues = new ArrayList<>();
                    JSONArray lineMetaArray = (JSONArray) parameterValue;
                    int size = lineMetaArray.size();
                    for (int index = 0; index < size; index++) {
                        parameterMutiValues.add(lineMetaArray.get(index));
                    }
                    dimValue.add(parameterMutiValues);
                } else if (parameterValue instanceof String && RegExpUtil.isExp((String) parameterValue)) {
                    Object handleResult = BuiltInAviatorEvaluator.execute(String.valueOf(parameterValue), Maps.newHashMap());
                    List<Map<String, Object>> csvParameters = (List<Map<String, Object>>) handleResult;
                    List<Object> parameterMutiValues = new ArrayList<>();
                    for (Map<String, Object> objectMap : csvParameters) {
                        for (Map.Entry<String, Object> csvParameter : objectMap.entrySet()) {
                            parameterMutiValues.add(csvParameter);
                        }
                    }
                    dimValue.add(parameterMutiValues);
                } else {
                    String exceptionMsg = String.format("The parameter value data type is invalid. It can only be String or List");
                    throw new DefinedException(exceptionMsg);
                }
                List<List<Object>> result = new ArrayList<>();
                descartes(dimValue, result, 0, new ArrayList<>());

            }
        }


        return parameters;
    }

    /**
     * association parameterized
     *
     * @param paramsName
     * @param paramValues
     * @return
     */
    private List<Map<String, Object>> associationParameterized(String[] paramsName, Object paramValues) {
        List<Map<String, Object>> parameters = new ArrayList<>();
        if (paramValues instanceof JSONArray) {
            JSONArray groupArray = (JSONArray) paramValues;
            for (int i = 0; i < groupArray.size(); i++) {
                Object lineArray = groupArray.get(i);
                if (lineArray instanceof JSONArray) {
                    JSONArray lineMetaArray = (JSONArray) lineArray;
                    int size = lineMetaArray.size();
                    if (paramsName.length < size) {
                        String exceptionMsg = String.format("The number of parameter values should not be greater than the number of parameter lists");
                        throw new DefinedException(exceptionMsg);
                    }
                    Map<String, Object> oneGroupValue = Maps.newHashMap();
                    for (int index = 0; index < size; index++) {
                        String paramName = paramsName[index];
                        Object paramValue = lineMetaArray.get(index);
                        oneGroupValue.put(paramName, Objects.isNull(paramValue) ? "" : paramValue);
                    }
                    parameters.add(oneGroupValue);
                } else if (lineArray instanceof String && RegExpUtil.isExp((String) lineArray)) {
                    Object handleResult = BuiltInAviatorEvaluator.execute(String.valueOf(lineArray), Maps.newHashMap());
                    return (List<Map<String, Object>>) handleResult;
                } else {
                    String exceptionMsg = String.format("The parameter value data type is invalid. It can only be String or List");
                    throw new DefinedException(exceptionMsg);
                }
            }
        }
        return parameters;
    }


    /**
     * Descartes
     *
     * @param dimValue
     * @param result
     * @param layer
     * @param curList
     */
    private void descartes(List<List<Object>> dimValue,
                           List<List<Object>> result, int layer, List<Object> curList) {
        if (layer < dimValue.size() - 1) {
            if (dimValue.get(layer).size() == 0) {
                descartes(dimValue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimValue.get(layer).size(); i++) {
                    List<Object> list = new ArrayList<>(curList);
                    list.add(dimValue.get(layer).get(i));
                    descartes(dimValue, result, layer + 1, list);
                }
            }
        } else if (layer == dimValue.size() - 1) {
            if (dimValue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimValue.get(layer).size(); i++) {
                    List<Object> list = new ArrayList<>(curList);
                    list.add(dimValue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }
}
