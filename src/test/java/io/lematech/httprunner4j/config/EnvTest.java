package io.lematech.httprunner4j.config;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.utils.ExpressHandler;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

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
public class EnvTest {
    @Test
    public void testYamlLoad(){
        Env.setEnv("user","arkhe");
        log.info(Env.getEnv("user"));
        log.info(Env.getEnv("prod"));
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
