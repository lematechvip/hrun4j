package vip.lematech.httprunner4j.testproject.testcases.rap2;

import vip.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.httprunner4j.entity.testcase.TestCase;
import vip.lematech.httprunner4j.testproject.HttpRunner4j;

import org.testng.annotations.Test;

/**
* @author lematech@foxmail.com
* @version 1.0.0
*/
public class MockTest extends HttpRunner4j {
    /**
    *
    * @param testCase
    */
    @Test(dataProvider = "dataProvider")
    public void rap2Mock(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
