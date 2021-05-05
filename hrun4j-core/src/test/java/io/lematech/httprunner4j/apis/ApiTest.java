package io.lematech.httprunner4j.apis;

import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ApiTest
 * @description TODO
 * @created 2021/4/21 2:00 下午
 * @publicWechat lematech
 */
public class ApiTest extends TestBase {
    /**
     * 简单案例
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void getToken(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

}
