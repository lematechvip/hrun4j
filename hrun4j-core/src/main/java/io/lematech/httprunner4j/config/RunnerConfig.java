package io.lematech.httprunner4j.config;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import lombok.Data;

import java.io.File;
import java.util.*;

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

    public List<File> getTestCasePaths() {
        return testCasePaths;
    }

    /**
     * run model
     * default 0： Standard Java project
     * 1： non Standard Java project
     * 2: platform model
     */
    private int runMode = 1;

    public File getWorkDirectory() {
        return Objects.isNull(this.workDirectory) ? new File(".") : this.workDirectory;
    }

    /**
     * work directory
     */
    private File workDirectory;

    public String getPkgName() {
        return StrUtil.isEmpty(this.pkgName) ? Constant.SELF_ROOT_PKG_NAME : this.pkgName;
    }

    /**
     * package name
     */
    private String pkgName;
    /**
     * testcase extension name
     */
    private String testCaseExtName;
    /**
     * test case paths
     */
    private List<File> testCasePaths;

    private static RunnerConfig instance = new RunnerConfig();

    public void setPkgName(String pkgName) {
        if (JavaIdentifierUtil.isValidJavaFullClassName(pkgName)) {
            this.pkgName = pkgName;
        } else {
            String exceptionMsg = String.format("pkg name {} is invalid,not apply java identifier,please modify it", pkgName);
            throw new DefinedException(exceptionMsg);
        }
    }
    private RunnerConfig() {
        testCasePaths = new ArrayList<>();
        testCaseExtName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;
    }
    public static RunnerConfig getInstance(){
        return instance;
    }

}
