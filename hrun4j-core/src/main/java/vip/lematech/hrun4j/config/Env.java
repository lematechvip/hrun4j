package vip.lematech.hrun4j.config;

import cn.hutool.core.io.FileUtil;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.common.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class Env {

    private RunnerConfig runnerConfig;

    public Env(RunnerConfig runnerConfig) {
        this.runnerConfig = runnerConfig;
    }

    public Map<String, Object> getEnvMap() {
        if (envMap == null) {
            initializeEnv();
        }
        return envMap;
    }

    private Map<String, Object> envMap;

    public synchronized void initializeEnv() {
        if (envMap == null) {
            RunnerConfig.RunMode runMode = runnerConfig.getRunMode();
            envMap = new HashMap<>();
            envMap.putAll(System.getenv());

            // 平台模式不需要加载其他环境变量
            if (runMode == RunnerConfig.RunMode.PLATFORM) return;
            Properties properties = new Properties();
            try {
                String envFilePath = (runMode == RunnerConfig.RunMode.POM) ? Constant.ENV_FILE_NAME : runnerConfig.getDotEnvPath();
                File searchFile = null;
                if (runMode == RunnerConfig.RunMode.CLI) {
                    if(Objects.isNull(envFilePath)){
                        return ;
                    }
                    if (FileUtil.isAbsolutePath(envFilePath)) {
                        searchFile = new File(envFilePath);
                    } else {
                        searchFile = new File(runnerConfig.getWorkDirectory()
                                , FilesHelper.filePathDecode(envFilePath));
                    }
                } else if (runMode == RunnerConfig.RunMode.POM) {
                    URL url = Thread.currentThread().getContextClassLoader().getResource(envFilePath);
                    searchFile = new File(FilesHelper.filePathDecode(url.getPath()));
                }
                if(FileUtil.exist(searchFile) && !searchFile.isDirectory()){
                    properties.load(new FileInputStream(searchFile));
                    envMap.putAll((Map) properties);
                }else{
                    String exceptionMsg = Constant.ENV_FILE_NAME + " is not exist";
                    LogHelper.warn(exceptionMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogHelper.error("An error occurred loading the.env file");
            }
        }

    }


    public void setEnv(String key, Object value) {
        if (envMap == null) {
            initializeEnv();
        }
        envMap.put(key, value);
    }

    public Object getEnv(String key) {
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
