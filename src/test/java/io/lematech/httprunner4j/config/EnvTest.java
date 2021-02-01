package io.lematech.httprunner4j.config;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.utils.AssertUtil;
import io.lematech.httprunner4j.utils.ExpressHandler;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
public class EnvTest<T> {
    @Test
    public void testYamlLoad() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {


      //  AbstractStringAssert test = assertThat("xx");

       // log.info("类型：{}",test.isEqualTo("xxxx"));
        Map<String,Object> configVars = new HashMap<>();
        configVars.put("check",1);
        configVars.put("expect",2);
        configVars.put("comparator","isLessThan");
        AssertUtil.assertObject(configVars);
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
    public static void main(String[] args) throws Exception {
        Class<?> clazz = EnvTest.class;
       // Method method = clazz.getMethod("test", Object.class); // 参数类型为 Object.class

        try {
//https://segmentfault.com/q/1010000017134039
            //https://segmentfault.com/q/1010000011015262
            //https://blog.csdn.net/u013604031/article/details/51006792
            Map<String,Object> configVars = new HashMap<>();
            configVars.put("check",1);
            configVars.put("expect",2);
            configVars.put("comparator","greaterThan");
            Object actual = configVars.get("check");
            Object expect = configVars.get("expect");
            String comparator = (String)configVars.get("comparator");
           // actual.getClass().getGenericType();
            Class expClz = expect.getClass();
            log.info("类型：{}",comparator.getClass());
            Class<?> clzValue = Class.forName("org.hamcrest.Matchers");
            Method method2 = clzValue.getMethod(comparator,Object.class);
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

    public <T> void test(T o) {
        System.out.println(o);
    }
    @Test
    public void testAssertRelect(T t){
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
            Method method2 = clzValue.getMethod(comparator,t.getClass());
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
    public void testReflect(){
        ExpressHandler handler = new ExpressHandler();
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
        handler.buildCurrentEnv(testContext,configVars,testStepVars);
        log.info(JSON.toJSONString(handler.getCurrentEnv()));
       RequestEntity requestEntity = new RequestEntity();
        Map<String,Object> header = new HashMap<>();
        header.put("headerKey","${add(1,2)}");
        Map<String,Object> parameters = new HashMap<>();
        requestEntity.setHeaders(header);
        parameters.put("parameterKey","${subtract(1,2)}");
        requestEntity.setParams(parameters);
        requestEntity.setUrl("/api/test/${add(a,b)}&${subtract(a,c)}&${divide(a,c)}");
        RequestEntity newReqEntity = (RequestEntity)handler.buildNewObj(requestEntity);
        log.info(JSON.toJSONString(newReqEntity));
    }
}
