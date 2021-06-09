package vip.lematech.httprunner4j.testcases.demojson;

import vip.lematech.httprunner4j.base.TestBase;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.config.RunnerConfig;
import vip.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DemoTest
 * @description TODO
 * @created 2021/3/23 10:58 上午
 * @publicWechat lematech
 */
public class DemoJsonTest extends TestBase {


    @BeforeClass
    public void configRunner() {
        RunnerConfig.getInstance().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME);
    }

    /**
     * 简单案例
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testSimpleSingleDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 复杂案例
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testComplexSingleDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 更复杂案例
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testMoreComplexSingleDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 测试变量优先级
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testVariablePriorityDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }



}