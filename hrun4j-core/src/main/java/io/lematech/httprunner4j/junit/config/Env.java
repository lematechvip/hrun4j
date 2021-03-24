package io.lematech.httprunner4j.junit.config;

import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.utils.PrintMap;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className EnvProperties
 * @description TODO
 * @created 2021/1/22 3:36 下午
 * @publicWechat lematech
 */
@Slf4j
public class Env {
    private static Map<String, String> envMap;
    private static synchronized void initializeEnv() {
        if (envMap == null) {
            envMap = new HashMap<>();
            envMap.putAll(System.getenv());
            Properties properties = new Properties();
            InputStream inputStream  = Env.class
                    .getClassLoader()
                    .getResourceAsStream(Constant.ENV_FILE_NAME);
            try {
                properties.load(inputStream);
                PrintMap.printMap(envMap);
                envMap.putAll((Map)properties);
                PrintMap.printMap(envMap);
            } catch (Exception e) {
                log.warn(Constant.ENV_FILE_NAME+" is not exist");
            }
        }
    }
    public static void setEnv(String key,String value) {
        if (envMap == null){
            initializeEnv();
        }
        envMap.put(key,value);
    }
    public static String getEnv(String key) {
        String value = null;
        if (envMap == null){
            initializeEnv();
        }
        if (envMap.containsKey(key)) {
            value = envMap.get(key);
        }
        return value;
    }
}
