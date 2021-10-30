package vip.lematech.hrun4j.config;

import cn.hutool.core.util.StrUtil;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import lombok.Data;

import java.io.File;
import java.util.*;

/**
 * Initialize the run configuration parameters
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

@Data
public class RunnerConfig {

    /**
     * internationalization support，support en/zh
     */
    private String i18n;

    public String getDotEnvPath() {
        return dotEnvPath;
    }

    public void setDotEnvPath(String dotEnvPath) {
        this.dotEnvPath = dotEnvPath;
    }

    /**
     * .env file path
     */
    private String dotEnvPath;


    /**
     * supports cli 、api integration
     */
    private RunMode runMode = RunMode.POM;

    public File getWorkDirectory() {
        return Objects.isNull(this.workDirectory) ? new File(Constant.DOT_PATH) : this.workDirectory;
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
    private List<String> testCasePaths;

    private static RunnerConfig instance = new RunnerConfig();

    /**
     * set package name
     * @param pkgName package name
     */
    public void setPkgName(String pkgName) {
        if (!JavaIdentifierHelper.isValidJavaFullClassName(pkgName)) {
            String exceptionMsg = String.format("The package name %s is invalid", pkgName);
            throw new DefinedException(exceptionMsg);
        }
        this.pkgName = pkgName;
    }

    private RunnerConfig() {
        testCasePaths = new ArrayList<>();
        testCaseExtName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;
    }

    public static RunnerConfig getInstance() {
        return instance;
    }

    /**
     * supports cli 、api integration
     *
     * @author lematech@foxmail.com
     * @version 1.0.1
     */
    public enum RunMode {
        CLI, POM, PLATFORM
    }
}
