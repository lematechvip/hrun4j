package io.lematech.httprunner4j.core.validator;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.Comparator;
import io.lematech.httprunner4j.utils.JsonUtil;
import io.lematech.httprunner4j.utils.RegExpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;
import org.testng.collections.Maps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className AssertChecker
 * @description TODO
 * @created 2021/1/22 4:07 下午
 * @publicWechat lematech
 */


@Slf4j
public class AssertChecker {
    private static Map<String,String> alisaMap = new HashMap<>();
    private static Matcher buildMatcherObj(String comparatorName,List<String> params,Object expect){
        Object obj = null;
        try {
            Class<?> clzValue = Class.forName("org.hamcrest.Matchers");
            String methodName = alisaMap.containsKey(comparatorName) ? alisaMap.get(comparatorName) : comparatorName;
            Method method = clzValue.getMethod(methodName,Class.forName(params.get(0)));
            obj = method.invoke(null,expect);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (Matcher)obj;
    }

    /**
     *
     * @param objectMap
     * @param responseEntity
     */
    public static void assertObject(Map<String, Object> objectMap, ResponseEntity responseEntity) {
        Map<String, List> methodAlisaMap = comparatorAlisaMap();
        Comparator comparator = new Comparator();
        if (objectMap.containsKey("check") && objectMap.containsKey("expect")) {
            comparator = JSON.parseObject(JSON.toJSONString(objectMap), Comparator.class);
        } else {
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                comparator.setComparator(entry.getKey());
                Object objValue = entry.getValue();
                if (objValue instanceof List) {
                    List<Object> objValues = (List) objValue;
                    if (objValues.size() == 2) {
                        comparator.setCheck(String.valueOf(objValues.get(0)));
                        comparator.setExpect(objValues.get(1));
                    } else {
                        String exceptionMsg = "校验表达式格式有误";
                        throw new DefinedException(exceptionMsg);
                    }
                }
            }
        }
        String comparatorName = comparator.getComparator();
        if (StrUtil.isEmpty(comparatorName)) {
            throw new DefinedException("比较器名称不能为空");
        }
        if (!methodAlisaMap.containsKey(comparatorName)) {
            throw new DefinedException(String.format("当前不支持 %s 比较器，已支持方法名称列表：%s", comparatorName, methodAlisaMap));
        }
        String exp = comparator.getCheck();
        Object actual = dataTransfer(exp, responseEntity);
        log.debug("表达式：{},提取结果：{}", exp, actual);
        try {
            Class<?> clz = Class.forName("org.junit.Assert");
            Method method = clz.getMethod("assertThat", Object.class, Matcher.class);
            method.invoke(null, comparator.getExpect()
                    , buildMatcherObj(comparatorName, methodAlisaMap.get(comparatorName), actual));
            log.info("检查点：{},预期值：{},实际值：{},校验结果：通过", exp, comparator.getExpect(), actual);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException targetException) {
            log.error("检查点：{}，预期值：{}，实际值：{}，校验结果：失败", exp, comparator.getExpect(), actual);
            String exceptionMsg = String.format("检查点：%s，校验失败，%s", comparator.getCheck(), targetException.getCause().toString());
            throw new AssertionError(exceptionMsg);
        }
        return;
    }

    /**
     * support regex/jsonpath/jmespath expression evaluator
     *
     * @param exp
     * @param responseEntity
     * @return
     */
    public static Object dataTransfer(String exp, ResponseEntity responseEntity) {
        if (StringUtils.isEmpty(exp)) {
            return "";
        }
        String respStr = JSON.toJSONString(responseEntity);
        /**
         * expression evaluation: sum(a+b)
         */
        if (RegExpUtil.isExp(exp)) {
            return RegExpUtil.buildNewString(exp, Maps.newHashMap());
        } else if (exp.startsWith("^") && exp.endsWith("$")) {
            String regSearch = RegExpUtil.findString(exp, respStr);
            return regSearch;
        } else if (exp.startsWith("$.")) {
            return JsonUtil.getJsonPathResult(exp, respStr);
        } else {
            JsonNode jsonNode;
            try {
                jsonNode = JsonUtil.getJmesPathResult(exp, respStr);
                return typeTransfer(jsonNode);
            } catch (Exception e) {
                return exp;
            }
        }
    }

    private static Object typeTransfer(JsonNode jsonNode) {
        if (jsonNode.isBoolean()) {
            return jsonNode.asBoolean();
        } else if (jsonNode.isDouble() || jsonNode.isFloat()) {
            return jsonNode.asDouble();
        } else if (jsonNode.isInt()) {
            return jsonNode.asInt();
        } else {
            return jsonNode.asText();
        }
    }

    public static void assertList(List<Map<String, Object>> mapList, ResponseEntity responseEntity) {
        if (Objects.isNull(mapList)) {
            return;
        }
        for (Map<String, Object> objectMap : mapList) {
            assertObject(objectMap, responseEntity);
        }
    }

    public static Map<String, List> comparatorAlisaMap() {
        Map<String, List> methodMap = new HashMap<>();
        try {
            Class matcherClz = Class.forName("org.hamcrest.Matchers");
            Method [] methods = matcherClz.getDeclaredMethods();
            for(Method method : methods){
                Type[] types = method.getParameterTypes();
                String methodName = method.getName();
                List<String> typeList = new ArrayList<>();
                for(Type type : types){
                    typeList.add(type.getTypeName());
                }
                methodMap.put(methodName,typeList);
                if (isSetAlisa(methodName)) {
                    String methodAlisa = transferMethodAlisa(methodName);
                    alisaMap.put(methodAlisa,methodName);
                    Integer parameterSize = typeList.size();
                    if(parameterSize == 1){
                        methodMap.put(methodAlisa,typeList);
                    }else {
                        String overrideMethodName = String.format("%s_%s",methodAlisa,parameterSize);
                        methodMap.put(overrideMethodName,typeList);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return methodMap;
    }
    private static boolean isSetAlisa(String methodName){
        boolean flag = true;
        if(methodName.length() <= 5){
            flag = false;
        }
        return flag;
    }
    private static String transferMethodAlisa(String methodName){
        StringBuilder methodAlisa = new StringBuilder();
        char[] chars = methodName.toCharArray();
        for(int index=0 ;index<chars.length ; index++){
            char letter = chars[index];
            if(index == 0){
                methodAlisa.append(letter);
            }else{
                if(Character.isUpperCase(letter)){
                    methodAlisa.append(letter);
                }
            }
        }
        String simpleMethodName = methodAlisa.toString().toLowerCase();
        return simpleMethodName;
    }
}
