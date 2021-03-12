package io.lematech.httprunner4j;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
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
        Map<String,List> methodAlisaMap = comparatorAlisaMap();
        if (objectMap.containsKey("check") && objectMap.containsKey("expect")){
            io.lematech.httprunner4j.entity.testcase.Comparator comparator = JSON.parseObject(JSON.toJSONString(objectMap), Comparator.class);
            String comparatorName = comparator.getComparator();
            if(StrUtil.isEmpty(comparatorName)){
                throw new DefinedException("比较器名称不能为空");
            }
            if(!methodAlisaMap.containsKey(comparatorName)){
                throw new DefinedException(String.format("当前不支持 %s 比较器",comparatorName));
            }
            String exp = comparator.getCheck();
            JsonNode jsonNode = JsonUtil.getJmesPathResult(exp,JSON.toJSONString(responseEntity));
            log.info("节点类型：{}",jsonNode.getNodeType());
            String actual = jsonNode.asText();
            log.info("表达式：{},提取结果：{}",exp,actual);
            try {
                Class<?> clz = Class.forName("org.junit.Assert");
                Method method = clz.getMethod("assertThat",Object.class, Matcher.class);
                method.invoke(null,actual
                        ,buildMatcherObj(comparatorName,methodAlisaMap.get(comparatorName),comparator.getExpect()));
                log.info("断言成功");
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException targetException) {
                String exceptionMsg = targetException.getCause().toString();
                throw new AssertionError(exceptionMsg);
            }
        }
        return;
    }

    /**
     * support regex/jsonpath/jmespath expression evaluator
     * @param exp
     * @param responseEntity
     * @return
     */
    public static String dataTransfer(String exp, ResponseEntity responseEntity){
        if(StringUtils.isEmpty(exp)){
            return "";
        }
        String respStr = JSON.toJSONString(responseEntity);
        if(RegExpUtil.isExp(exp)){
            return RegExpUtil.buildNewString(exp, Maps.newHashMap());
        }else if(exp.startsWith("^")&&exp.endsWith("$")){
          String regSearch = RegExpUtil.findString(exp,respStr);
          return regSearch;
        }else if(exp.startsWith("$.")){
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(respStr);
            Object searchResult = JsonPath.read(document, exp);
            log.info("查询结果：{}",searchResult);
        }else {
            JsonNode jsonNode;
            try{
                jsonNode = JsonUtil.getJmesPathResult(exp,respStr);
                log.info("节点类型：{}",jsonNode.getNodeType());
                exp = jsonNode.asText();
            }catch (Exception e){
               return exp;
            }
        }
        return exp;
    }
    public static void assertList(List<Map<String,Object>> mapList,ResponseEntity responseEntity){
        if(Objects.isNull(mapList)){
            return;
        }
        for(Map<String,Object> objectMap:mapList){
            assertObject(objectMap,responseEntity);
        }
    }

    public static Map<String,List> comparatorAlisaMap(){
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
