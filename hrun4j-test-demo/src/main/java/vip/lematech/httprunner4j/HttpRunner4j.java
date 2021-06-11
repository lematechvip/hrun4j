package vip.lematech.httprunner4j;

import com.googlecode.aviator.AviatorEvaluator;
import org.testng.annotations.BeforeSuite;
import vip.lematech.httprunner4j.base.TestBase;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.config.RunnerConfig;
import vip.lematech.httprunner4j.functions.MyFunction;
import vip.lematech.httprunner4j.helper.LogHelper;

/**
 * Extension function
 *
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class HttpRunner4j extends TestBase {

    @Override
    @BeforeSuite
    public void beforeSuite() {
        LogHelper.info(" Add function to static code block !");
        AviatorEvaluator.addFunction(new MyFunction.SetupHookFunction());
        AviatorEvaluator.addFunction(new MyFunction.TearDownHookFunction());
        AviatorEvaluator.addFunction(new MyFunction.SignGenerateFunction());
        /**
         * 包名，资源路径下查找测试用例前置，默认：vip.lematech.httprunner4j
         */
        RunnerConfig.getInstance().setPkgName("vip.lematech.httprunner4j");
        /**
         * Test case file suffix
         */
        RunnerConfig.getInstance().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME);
    }
}

