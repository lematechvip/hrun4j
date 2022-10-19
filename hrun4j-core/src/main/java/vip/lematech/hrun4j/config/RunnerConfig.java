package vip.lematech.hrun4j.config;

import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import vip.lematech.hrun4j.core.processor.BuiltInAviatorEvaluator;
import vip.lematech.hrun4j.core.runner.TestCaseRunner;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import lombok.Data;
import vip.lematech.hrun4j.helper.LogHelper;

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

    public static final String DEFAULT_TEST_SUITE_NAME = "hrun4j";

    /**
     * internationalization support，support en/zh
     */
    public static String i18n;

    /**
     *
     */
    private String testSuiteName;

    /**
     *
     */
    private TestCaseRunner testCaseRunner;

    /**
     *
     */
    private AviatorEvaluatorInstance aviatorEvaluatorInstance;

    /**
     *
     */
    private Env env;

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

    private static RunnerConfig instance = new RunnerConfig(DEFAULT_TEST_SUITE_NAME);

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

    private RunnerConfig(String testSuiteName) {
        this.testSuiteName = testSuiteName;
        this.env = new Env(this);
        testCasePaths = new ArrayList<>();
        testCaseExtName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;
        aviatorEvaluatorInstance = AviatorEvaluator.newInstance();
        testCaseRunner = new TestCaseRunner(this);
        init();
    }

    private void init() {
        aviatorEvaluatorInstance.addFunction(new BuiltInAviatorEvaluator.BuiltInFunctionEnv(this));
        aviatorEvaluatorInstance.addFunction(new BuiltInAviatorEvaluator.BuiltInFunctionParameterize(this));
        aviatorEvaluatorInstance.addFunction(new BuiltInAviatorEvaluator.BuiltInFunctionHelloWorld(this));
        aviatorEvaluatorInstance.addFunction(new BuiltInAviatorEvaluator.BuiltInFunctionBeanShell(this));
    }

    public void addBuiltInAviatorEvaluator(BuiltInAviatorEvaluator.BaseBuiltInFunction baseBuiltInFunction) {
        aviatorEvaluatorInstance.addFunction(baseBuiltInFunction);
    }

    public static RunnerConfig getInstance() {
        return instance;
    }

    public static RunnerConfig getInstanceBySuiteName(String testSuiteName) {
        LogHelper.info("==xxx==={}====", testSuiteName);
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
