package vip.lematech.hrun4j.base;

import cn.hutool.core.io.FileUtil;
import org.testng.ITestContext;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.core.provider.NGDataProvider;
import vip.lematech.hrun4j.core.runner.TestCaseRunner;
import vip.lematech.hrun4j.helper.LogHelper;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Test base classes for pre -, post -, and data loading
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 *
 */

public class TestBase {
    private String testCaseName;
    private String testSuiteName;

    @BeforeSuite
    public void beforeSuite(ITestContext context){
        this.testSuiteName = context.getSuite().getName();
        LogHelper.info("[========================================]@beforeSuite()");
    }
    @BeforeMethod
    public void setUp() {
        LogHelper.info("[====================" + this.testCaseName + "====================]@START");
    }
    @AfterMethod
    public void tearDown() {
        LogHelper.info("[====================" + this.testCaseName + "====================]@END");
    }
    @AfterSuite
    public void afterSuite(){
        LogHelper.info("[========================================]@afterSuite()");
    }
    @DataProvider
    public Object[][] dataProvider(ITestContext context, Method method) {
        if (Objects.isNull(testSuiteName)) {
            this.testSuiteName = context.getSuite().getName();
        }
        Object[][] objects;
        this.testCaseName = method.getName();

        String packageName = FileUtil.mainName(method.getDeclaringClass().getName());
        try {
            objects = new NGDataProvider(testSuiteName).dataProvider(packageName, testCaseName);
        } catch (DefinedException e) {
            throw e;
        } catch (Exception e) {
            String exceptionMsg = String.format("Abnormal testng data loading occurs, and the reason for the exception is as follows: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return objects;
    }

    public RunnerConfig getRunnerConfig() {
        return RunnerConfig.getInstanceBySuiteName(this.testSuiteName);
    }

    public TestCaseRunner getTestCaseRunner() {
        return getRunnerConfig().getTestCaseRunner();
    }
}
