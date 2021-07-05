package vip.lematech.hrun4j.testcases.postman.post;

import org.testng.annotations.Test;
import vip.lematech.hrun4j.Hrun4j;
import vip.lematech.hrun4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.hrun4j.entity.testcase.TestCase;


/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class PostTest extends Hrun4j {
    @Test(dataProvider = "dataProvider")
    public void postScene(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
