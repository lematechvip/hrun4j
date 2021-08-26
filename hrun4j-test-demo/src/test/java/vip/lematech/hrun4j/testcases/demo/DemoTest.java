package vip.lematech.hrun4j.testcases.demo;

import vip.lematech.hrun4j.Hrun4j;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import vip.lematech.hrun4j.common.Constant;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * @className DemoTest
 * @description TODO
 * @created 2021/3/23 10:58 上午
 *
 * @publicWechat lematech
 */
public class DemoTest extends Hrun4j {

    @BeforeClass
    public void configRunner() {
        RunnerConfig.getInstance().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME);
    }


    /**
     * 测试接口定义引用
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void 测试接口定义应用中文(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
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

    /**
     * 测试接口定义引用
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

    /**
     * 测试多种数据提取
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testMultipleGetDataDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


    /**
     * 测试指定步骤下的request和response信息输出
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testCurrentStepRequestAndResponseDataDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


    /**
     * 测试应用环境变量案例
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testReferenceEnvDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


    /**
     * 支持自定义aviator表达式及运算比较
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testAviatorExpDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


    /**
     * 测试从文件中读取数据
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testDataProviderFromCsvDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 测试从文件中读取数据
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testDataAssociatedProviderDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 测试从文件中读取数据
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testBeanShellDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 测试从文件中读取数据
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testJsonSchemaValidateTest(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


    /**
     * json data validation
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void testJsonValidateTest(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }



    /**
     * 测试文件下载
     * @param testCase
     *
     */
   /* @Test(dataProvider = "dataProvider")
    public void testFileDownloadDemo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
*/
}
