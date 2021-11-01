package vip.lematech.hrun4j.core.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.helper.JsonHelper;
import vip.lematech.hrun4j.helper.RegExpHelper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.core.processor.ExpProcessor;

import java.util.*;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class DataConstructor {

    private ExpProcessor expProcessor;

    public DataConstructor() {
        this.expProcessor = new ExpProcessor();
    }

    /**
     * Parameterization, which supports associating parameters, reading from files, or dynamically generating expressions
     *
     * @param parameterObj The parameter object
     * @return data list
     */
    public List<Map<String, Object>> parameterized(Object parameterObj) {
        List<Map<String, Object>> parameters = new ArrayList<>();
        if (Objects.isNull(parameterObj)) {
            return parameters;
        }
        JSONObject jsonObject = null;
        if (parameterObj instanceof JSONObject) {
            jsonObject = (JSONObject) parameterObj;
        }else {
            String paramsStr = JSON.toJSONString(parameterObj);
            if (!JsonHelper.isJson(paramsStr)) {
                String exceptionMsg = String.format("The parameters data %s json format is incorrect", paramsStr);
                throw new DefinedException(exceptionMsg);
            }
            jsonObject = JSONObject.parseObject(paramsStr);
        }

        boolean isAssociationParameter = false;
        List<List<Object>> dimValue = new ArrayList<>();
        int paramNameIndex = 0;
        Map<Integer, String> paramNameIndexMap = Maps.newHashMap();
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
                isAssociationParameter = true;
                return associationParameterized(paramName, parameterValue);
            } else {
                paramNameIndexMap.put(paramNameIndex++, paramsName);
                nonAssociationParameterized(dimValue, parameterValue);
            }
        }
        List<List<Object>> result = new ArrayList<>();
        descartes(dimValue, result, 0, new ArrayList<>());
        for (List objList : result) {
            Map<String, Object> parameter = Maps.newHashMap();
            for (int index = 0; index < objList.size(); index++) {
                String paramName = paramNameIndexMap.get(index);
                Object paramValue = objList.get(index);
                parameter.put(paramName, paramValue);
            }
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * Non association parameterized
     *
     * @param dimValue
     * @param parameterValue
     */
    private void nonAssociationParameterized(List<List<Object>> dimValue, Object parameterValue) {
        if (parameterValue instanceof JSONArray) {
            List<Object> parameterMutiValues = new ArrayList<>();
            JSONArray lineMetaArray = (JSONArray) parameterValue;
            int size = lineMetaArray.size();
            if (size == 1 && lineMetaArray.get(0) instanceof String) {
                String expValue = (String) lineMetaArray.get(0);
                if (RegExpHelper.isExp(expValue)) {
                    Object handleResult = handleParameterizeExp(expValue);
                    List<Map<String, Object>> csvParameters = (List<Map<String, Object>>) handleResult;
                    for (Map<String, Object> objectMap : csvParameters) {
                        for (Map.Entry<String, Object> csvParameter : objectMap.entrySet()) {
                            parameterMutiValues.add(csvParameter);
                        }
                    }
                } else {
                    parameterMutiValues.add(expValue);
                }
            } else {
                for (int index = 0; index < size; index++) {
                    parameterMutiValues.add(lineMetaArray.get(index));
                }
            }
            dimValue.add(parameterMutiValues);
        } else if (parameterValue instanceof String && RegExpHelper.isExp((String) parameterValue)) {
            Object handleResult = handleParameterizeExp((String) parameterValue);
            List<Map<String, Object>> csvParameters = (List<Map<String, Object>>) handleResult;
            List<Object> parameterMutiValues = new ArrayList<>();
            for (Map<String, Object> objectMap : csvParameters) {
                for (Map.Entry<String, Object> csvParameter : objectMap.entrySet()) {
                    parameterMutiValues.add(csvParameter);
                }
            }
            dimValue.add(parameterMutiValues);
        } else if (parameterValue instanceof String) {
            List<Object> onlyOne = new ArrayList<>();
            onlyOne.add(parameterValue);
            dimValue.add(onlyOne);
        } else {
            String exceptionMsg = String.format("The parameter value data type is invalid. It can only be String or List");
            throw new DefinedException(exceptionMsg);
        }
    }

    private Object handleParameterizeExp(String parameterValue) {
        String expValue = parameterValue;
        Object handleResult;
        if (RegExpHelper.isParameterizeExp(expValue)) {
            String filePathValue = RegExpHelper.findString(Constant.REGEX_PARAMETERIZE_EXPRESSION, expValue);
            Map environment = Maps.newHashMap();
            environment.put(Constant.CSV_FILE_PATH_KEY, filePathValue);
            String aliasFileExp = String.format("${P(%s)}", Constant.CSV_FILE_PATH_KEY);
            handleResult = this.expProcessor.handleStringExp(aliasFileExp, environment);
        } else {
            handleResult = this.expProcessor.handleStringExp(expValue);
        }
        return handleResult;
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
                }
            }
        } else if (paramValues instanceof String && RegExpHelper.isExp((String) paramValues)) {
            Object handleResult = handleParameterizeExp((String) paramValues);
            return (List<Map<String, Object>>) handleResult;
        } else {
            String exceptionMsg = String.format("The parameter value data type is invalid. It can only be String or List");
            throw new DefinedException(exceptionMsg);
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
