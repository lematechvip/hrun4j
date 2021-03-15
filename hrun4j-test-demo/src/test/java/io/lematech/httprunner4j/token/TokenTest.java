package io.lematech.httprunner4j.token;

import io.lematech.httprunner4j.TestCaseExecutorEngine;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TokenTest
 * @description TODO
 * @created 2021/3/4 10:54 上午
 * @publicWechat lematech
 */
public class TokenTest extends TestBase {
    @Test(dataProvider = "dataProvider")
    public void getToken(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
