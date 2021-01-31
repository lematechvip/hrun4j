package io.lematech.httprunner4j.utils;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.entity.testcase.Comparator;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
public class AssertUtil {
    public static void assertObject(Map<String, Object> objectMap) {

        if (objectMap.containsKey("check") && objectMap.containsKey("expect")){
//        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//        }
            Comparator comparator = JSON.parseObject(JSON.toJSONString(objectMap), Comparator.class);
            try {
                //https://segmentfault.com/q/1010000017134039
                Map<String,Object> configVars = new HashMap<>();
                configVars.put("check",1);
                configVars.put("expect",2);
                configVars.put("comparator","greaterThan");
                Object actual = configVars.get("check");
                Object expect = configVars.get("expect");
                //String comparator = (String)configVars.get("comparator");
                Class expClz = expect.getClass();
                log.info("类型：{}",comparator.getClass());
                Class<?> clzValue = Class.forName("org.hamcrest.Matchers");
                log.info("类型：{}",clzValue.getCanonicalName());
                Method method2 = clzValue.getMethod(String.valueOf(comparator), Object.class);
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
        return;
    }

    public static void assertList(List<Map<String,Object>> mapList){
        for(Map<String,Object> objectMap:mapList){
            assertObject(objectMap);
        }
    }


}
