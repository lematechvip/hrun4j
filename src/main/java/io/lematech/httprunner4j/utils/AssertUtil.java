package io.lematech.httprunner4j.utils;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.entity.testcase.Comparator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractAssert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
/**
 *
 */
@Slf4j
public class AssertUtil {
    public static void assertObject(Map<String,Object> objectMap) {

        if (objectMap.containsKey("check") && objectMap.containsKey("expect")){
//        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//        }
            Comparator comparator = JSON.parseObject(JSON.toJSONString(objectMap), Comparator.class);
            //org.hamcrest.MatcherAssert.ase
           // Matcher
            Class<?> threadClazz = null;
                try {


                    assertThat("xxx", startsWith("Ma"));
                    threadClazz = Class.forName("org.assertj.core.api.Assertions");
                    //String comExpect = String.format("%s%s",comparator.getComparator(),comparator.getExpect());
                    Method method = threadClazz.getMethod("assertThat", Object.class);
                    AbstractAssert result = (AbstractAssert)method.invoke(null,comparator.getExpect());
                    log.info("result：{}",result);
                    //org.assertj.core.api.ObjectAssert;
                    //Object object = method.invoke(null,comparator.getCheck());
                    //org.assertj.core.api.Assertions.
                    Method method1 = result.getClass().getGenericSuperclass().getClass().getMethod(comparator.getComparator());
                    //ObjectAssert.
                    // org.assertj.core.api.Assertions.
                    //Assertions assertions = new Assertions();

                    Object x = method1.invoke(comparator.getExpect());
                    log.info("result：{}",x);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        }
    }

    public static void assertList(List<Map<String,Object>> mapList){
        for(Map<String,Object> objectMap:mapList){
            assertObject(objectMap);
        }
    }


}
