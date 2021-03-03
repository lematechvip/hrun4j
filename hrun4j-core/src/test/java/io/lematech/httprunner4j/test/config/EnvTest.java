package io.lematech.httprunner4j.test.config;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.test.entity.http.RequestEntity;
import io.lematech.httprunner4j.test.utils.ExpressionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className EnvTest
 * @description TODO
 * @created 2021/1/22 3:55 下午
 * @publicWechat lematech
 */
@Slf4j
public class EnvTest {
    @Test
    public void testYamlLoad() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {


      //  AbstractStringAssert test = assertThat("xx");

       // log.info("类型：{}",test.isEqualTo("xxxx"));
        Map<String,Object> configVars = new HashMap<>();
        configVars.put("check",1);
        configVars.put("expect",2);
        configVars.put("comparator","isLessThan");
        //AssertUtil.assertObject(configVars);
        //Assert.assertEquals(1,2);

       /* Class clz = Class.forName("org.testng.Assert");
        Method method = clz.getMethod("assertEquals",Object.class,Object.class);
       // Constructor constructor = clz.getConstructor();
       // Object object = constructor.newInstance();
        try{
            method.invoke(null, 1,2);
        }catch (InvocationTargetException targetException){
            log.info("异常信息：{}",targetException.getCause());
        }*/

        //org.testng.Assert.assertEquals();
        //method.invoke(2,2);

      /*  Class clz = Class.forName("com.chenshuyi.reflect.Apple");
        Method method = clz.getMethod("setPrice", int.class);
        Constructor constructor = clz.getConstructor();
        Object object = constructor.newInstance();
        method.invoke(object, 4);*/
    }

    //private Executor executor = new Executor();
    @Test
    public void testExecutor(){
       // executor.execute("hrun4j_demo_testcase.yml");
    }
    @Test
    public void testAssertRelect1(){
       // assertThat("xxx", startsWith("Ma"));
        try {
//https://segmentfault.com/q/1010000017134039
            Map<String,Object> configVars = new HashMap<>();
            configVars.put("check",1);
            configVars.put("expect",2);
            configVars.put("comparator","greaterThan");
            Object actual = configVars.get("check");
            Object expect = configVars.get("expect");
            String comparator = (String)configVars.get("comparator");
            Class expClz = expect.getClass();
           log.info("类型：{}",comparator.getClass());
            Class<?> clzValue = Class.forName("org.hamcrest.Matchers");
            Method method2 = clzValue.getMethod(comparator, Comparable.class);
            Object obj = method2.invoke(null,expect);
            Class<?> clz = Class.forName("org.junit.Assert");
            //org.hamcrest.Matchers.g
            Method method = clz.getMethod("assertThat",Object.class, Matcher.class);
            Object resObj = method.invoke(null,actual,obj);
            log.info("执行结果：{}",resObj);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException targetException) {
            targetException.printStackTrace();
            log.info("异常信息：{}",targetException.getCause());
        }
    }

    @Test
    public void testAssert() {
        try {
            Class<?> clzValue = Class.forName("org.hamcrest.Matchers");
            for (Method method : clzValue.getDeclaredMethods()) {
                log.info("方法名：{},方法参数：{}", method.getName(), method.getParameterTypes());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAssertRelect(){
            Map<String,Object> comp1 = new HashMap<>();
            comp1.put("expect","le1");
            comp1.put("comparator","sw");
            Map<String,Object> comp2 = new HashMap<>();
            comp2.put("expect","xin1");
            comp2.put("comparator","ew");
            List<Map<String,Object>> validateList = new ArrayList();
            validateList.add(comp1);
            validateList.add(comp2);
           // AssertUtil.assertList(validateList);

    }

    @Test
    public void testReflect(){
        ExpressionProcessor handler = new ExpressionProcessor();
        Map<String,Object> testContext = new HashMap<>();
        testContext.put("a",1);
        Map<String,Object> configVars = new HashMap<>();
        configVars.put("a",4);
        configVars.put("b",10);
        configVars.put("c",5);
        Map<String,Object> testStepVars = new HashMap<>();
        testStepVars.put("a",6);
        testStepVars.put("c",0);
        testStepVars.put("b",7);
       // handler.buildCurrentEnv(testContext,configVars,testStepVars);
       // log.info(JSON.toJSONString(handler.getCurrentEnv()));
       RequestEntity requestEntity = new RequestEntity();
        Map<String,Object> header = new HashMap<>();
        header.put("headerKey","${add(1,2)}");
        Map<String,Object> parameters = new HashMap<>();
        requestEntity.setHeaders(header);
        parameters.put("parameterKey","${subtract(1,2)}");
        requestEntity.setParams(parameters);
        requestEntity.setUrl("/api/test/${add(a,b)}&${subtract(a,c)}&${divide(a,c)}");
        //RequestEntity newReqEntity = (RequestEntity)handler.buildNewObj(requestEntity);
        //log.info(JSON.toJSONString(newReqEntity));
    }

    @Test
    public void testAlisaMap(){
        Map<String, List> methodMap = new HashMap<>();
        int simpleLength = 0;
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
                    simpleLength++;
                    String methodAlisa = transferMethodAlisa(methodName);
                    Integer parameterSize = typeList.size();
                    if(parameterSize == 1){
                        methodMap.put(methodAlisa,typeList);
                    }else {
                        String overrideMethodName = String.format("%s_%s",methodAlisa,parameterSize);
                        methodMap.put(overrideMethodName,typeList);
                    }
                }
            }
            log.info("总共有多少个方法：{},转换方法数：{},简写方法数：{},完整方法列表及简化列表：{}"
                    ,methods.length, methodMap.size(),simpleLength,JSON.toJSONString(methodMap));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private boolean isSetAlisa(String methodName){
        boolean flag = true;
        if(methodName.length() <= 5){
            flag = false;
        }
        return flag;
    }
    private String transferMethodAlisa(String methodName){
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
        log.info("方法名：{},简写：{}",methodName,simpleMethodName);
        return simpleMethodName;
    }
}
