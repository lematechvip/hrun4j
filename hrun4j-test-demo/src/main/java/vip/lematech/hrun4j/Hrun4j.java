package vip.lematech.hrun4j;


import com.googlecode.aviator.AviatorEvaluator;
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
    public void beforeSuite() {
        LogHelper.info(" Add function to static code block !");
        AviatorEvaluator.addFunction(new MyFunction.SetupHookFunction());
        AviatorEvaluator.addFunction(new MyFunction.TearDownHookFunction());
        AviatorEvaluator.addFunction(new MyFunction.SignGenerateFunction());
        AviatorEvaluator.addFunction(new MyFunction.DefinedFunctionAdd());
        AviatorEvaluator.addFunction(new MyFunction.DefinedFunctionDivide());
        AviatorEvaluator.addFunction(new MyFunction.DefinedFunctionMultiply());
        AviatorEvaluator.addFunction(new MyFunction.DefinedFunctionDivide());
        AviatorEvaluator.addFunction(new MyFunction.DefinedFunctionSubtract());
        AviatorEvaluator.addFunction(new MyFunction.RequestAndResponseHook());
        AviatorEvaluator.addFunction(new MyFunction.DefinedHookFunction());

        /**
         * 包名，资源路径下查找测试用例前置，默认：vip.lematech.hrun4j
         */
        RunnerConfig.getInstance().setPkgName("vip.lematech.hrun4j");
        /**
         * Test case file suffix
         */
        RunnerConfig.getInstance().setI18n(Constant.I18N_US);
        RunnerConfig.getInstance().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME);
    }
}

