package io.lematech.httprunner4j.demo;

import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DemoTest
 * @description TODO
 * @created 2021/3/23 10:58 上午
 * @publicWechat lematech
 */
public class DemoTest extends TestBase {

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

    /**
     * 测试变量优先级
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testApiReferenceDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 测试自定义request config 项是否正常运行
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testDefineRequestConfigDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 引用测试用例
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testTestCaseReferenceDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


}
