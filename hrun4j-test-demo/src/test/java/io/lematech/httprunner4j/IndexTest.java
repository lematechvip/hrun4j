package io.lematech.httprunner4j;

import io.lematech.httprunner4j.test.TestCaseExecutorEngine;
import io.lematech.httprunner4j.test.base.TestBase;
import io.lematech.httprunner4j.test.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TokenTest
 * @description TODO
 * @created 2021/3/3 7:40 下午
 * @publicWechat lematech
 */
public class IndexTest extends TestBase {

    @Test(dataProvider = "dataProvider")
    public void index(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
