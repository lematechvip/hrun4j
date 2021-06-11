package vip.lematech.httprunner4j.config;

import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.core.loader.Searcher;
import vip.lematech.httprunner4j.helper.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
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
                String envFilePath = (runMode == RunnerConfig.RunMode.POM) ? Constant.ENV_FILE_NAME : RunnerConfig.getInstance().getDotEnvPath();
                File envFile = new Searcher().quicklySearchFile(envFilePath);
                properties.load(new FileInputStream(envFile));
                envMap.putAll((Map) properties);
            } catch (Exception e) {
                String exceptionMsg = Constant.ENV_FILE_NAME + " is not exist";
                LogHelper.warn(exceptionMsg);
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
