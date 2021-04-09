package io.lematech.httprunner4j.config;

import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.utils.JavaIdentifierUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RunnerConfig
 * @description TODO
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */

@Data
public class RunnerConfig {

    /**
     * internationalization support，support en/zh
     */
    private String i18n;

    /**
     * run model
     * default 0： Standard Java project
     * 1： non Standard Java project
     */
    private Integer runMode;

    public List<String> getExecutePaths() {
        if (executePaths.isEmpty()) {
            executePaths.add(Constant.DOT_PATH);
        }
        return executePaths;
    }

    private String pkgName;
    private List<String> executePaths;
    private String testCaseExtName;
    private static RunnerConfig instance = new RunnerConfig();

    public void setPkgName(String pkgName) {
        if(JavaIdentifierUtil.isValidJavaFullClassName(pkgName)){
            this.pkgName = pkgName;
        }else{
            String exceptionMsg = String.format("pkc name {} is invalid,not apply java identifier,please modify it",pkgName);
            throw new DefinedException(exceptionMsg);
        }

    }
    private RunnerConfig() {
        executePaths = new ArrayList<>();
        testCaseExtName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;
    }
    public static RunnerConfig getInstance(){
        return instance;
    }

}
