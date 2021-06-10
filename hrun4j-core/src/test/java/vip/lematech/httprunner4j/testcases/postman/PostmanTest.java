package vip.lematech.httprunner4j.testcases.postman;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vip.lematech.httprunner4j.base.TestBase;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.config.RunnerConfig;
import vip.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.httprunner4j.entity.testcase.TestCase;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className PostmanTest
 * @description TODO
 * @created 2021/6/10 3:58 下午
 * @publicWechat lematech
 */
public class PostmanTest extends TestBase {
    @BeforeClass
    public void configRunner() {
        RunnerConfig.getInstance().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME);
    }

    /**
     * get request
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void get(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * get request
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void postRawText(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
