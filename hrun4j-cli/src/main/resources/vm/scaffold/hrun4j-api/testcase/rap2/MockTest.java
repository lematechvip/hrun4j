package io.lematech.firstproject.testcases.rap2;

import io.lematech.firstproject.testcases.HttpRunner4j;
import io.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className WeatherTest
 * @description TODO
 * @created 2021/5/25 9:30 上午
 * @publicWechat lematech
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
