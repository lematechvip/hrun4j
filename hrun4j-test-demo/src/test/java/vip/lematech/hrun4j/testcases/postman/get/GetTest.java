package vip.lematech.hrun4j.testcases.postman.get;

import org.testng.annotations.Test;
import vip.lematech.hrun4j.Hrun4j;
import vip.lematech.hrun4j.entity.testcase.TestCase;


/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class GetTest extends Hrun4j {
    @Test(dataProvider = "dataProvider")
    public void getScene(TestCase testCase) {
        getTestCaseRunner().execute(testCase);
    }
}
