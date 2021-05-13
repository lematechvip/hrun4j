package io.lematech.httprunner4j.config;

import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.loader.Searcher;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.widget.log.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
            RunnerConfig.RunMode runMode = RunnerConfig.getInstance().getRunMode();
            envMap = new HashMap<>();
            envMap.putAll(System.getenv());
            Properties properties = new Properties();
            try {
                String envFilePath = (runMode == RunnerConfig.RunMode.API) ? Constant.ENV_FILE_NAME : RunnerConfig.getInstance().getDotEnvPath();
                File envFile = new Searcher().quicklySearchFile(envFilePath);
                properties.load(new FileInputStream(envFile));
                envMap.putAll((Map) properties);
            } catch (Exception e) {
                String exceptionMsg = Constant.ENV_FILE_NAME + " is not exist";
                throw new DefinedException(exceptionMsg);
            }
        }
    }

    public static void setEnv(String key, Object value) {
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
