package io.lematech.httprunner4j.config;

import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.widget.log.MyLog;

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
public class Env {
    public static Map<String, Object> getEnvMap() {
        if (envMap == null) {
            initializeEnv();
        }
        return envMap;
    }

    private static Map<String, Object> envMap;

    private static synchronized void initializeEnv() {
        if (envMap == null) {
            envMap = new HashMap<>();
            envMap.putAll(System.getenv());
            Properties properties = new Properties();
            InputStream inputStream = Env.class
                    .getClassLoader()
                    .getResourceAsStream(Constant.ENV_FILE_NAME);
            try {
                properties.load(inputStream);
                envMap.putAll((Map)properties);
            } catch (Exception e) {
                MyLog.warn(Constant.ENV_FILE_NAME + " is not exist");
            }
        }
    }

    public static void setEnv(String key, String value) {
        if (envMap == null) {
            initializeEnv();
        }
        envMap.put(key, value);
    }

    public static Object getEnv(String key) {
        Object value = null;
        if (envMap == null) {
            initializeEnv();
        }
        if (envMap.containsKey(key)) {
            value = envMap.get(key);
        }
        return value;
    }
}
