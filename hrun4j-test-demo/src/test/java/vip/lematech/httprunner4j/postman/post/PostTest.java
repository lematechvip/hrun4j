package vip.lematech.httprunner4j.postman.post;

import org.testng.annotations.Test;
import vip.lematech.httprunner4j.HttpRunner4j;
import vip.lematech.httprunner4j.testcases.core.engine.TestCaseExecutorEngine;
import vip.lematech.httprunner4j.entity.testcase.TestCase;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class PostTest extends HttpRunner4j {
    @Test(dataProvider = "dataProvider")
    public void postScene(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
