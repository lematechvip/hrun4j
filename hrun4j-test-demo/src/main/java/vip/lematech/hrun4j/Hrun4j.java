package vip.lematech.hrun4j;


import com.googlecode.aviator.AviatorEvaluator;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.functions.MyFunction;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.base.TestBase;

/**
 * Extension function
 *
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class Hrun4j extends TestBase {

    @Override
    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        super.beforeSuite(context);
        LogHelper.info(" Add function to static code block !");
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.SetupHookFunction(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.TearDownHookFunction(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.SignGenerateFunction(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.DefinedFunctionAdd(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.DefinedFunctionDivide(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.DefinedFunctionMultiply(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.DefinedFunctionDivide(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.DefinedFunctionSubtract(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.RequestAndResponseHook(getRunnerConfig()));
        getRunnerConfig().addBuiltInAviatorEvaluator(new MyFunction.DefinedHookFunction(getRunnerConfig()));

        /**
         * 包名，资源路径下查找测试用例前置，默认：vip.lematech.hrun4j
         */
        getRunnerConfig().setPkgName("vip.lematech.hrun4j");
        /**
         * Test case file suffix
         */
        RunnerConfig.i18n = Constant.I18N_US;
        getRunnerConfig().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME);
    }
}

