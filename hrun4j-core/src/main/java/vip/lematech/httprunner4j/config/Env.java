package vip.lematech.httprunner4j.config;

import cn.hutool.core.io.FileUtil;
import vip.lematech.httprunner4j.base.TestBase;
import vip.lematech.httprunner4j.common.DefinedException;
import vip.lematech.httprunner4j.core.loader.Searcher;
import vip.lematech.httprunner4j.helper.FilesHelper;
import vip.lematech.httprunner4j.helper.LogHelper;
import vip.lematech.httprunner4j.common.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
                File searchFile = null;
                if (runMode == RunnerConfig.RunMode.CLI) {
                    if (FileUtil.isAbsolutePath(envFilePath)) {
                        searchFile = new File(envFilePath);
                    } else {
                        searchFile = new File(RunnerConfig.getInstance().getWorkDirectory()
                                , FilesHelper.filePathDecode(envFilePath));
                    }
                } else if (runMode == RunnerConfig.RunMode.POM) {
                    URL url = TestBase.class.getResource(envFilePath);
                    searchFile = new File(FilesHelper.filePathDecode(url.getPath()));
                }
                if(FileUtil.exist(searchFile)){
                    properties.load(new FileInputStream(searchFile));
                    envMap.putAll((Map) properties);
                }
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
