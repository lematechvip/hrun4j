
package io.lematech.httprunner4j;


import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

class TestcasesTest extends TestBase {
    @Test(dataProvider = "dataProvider")
    public void testcase(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
    @Test(dataProvider = "dataProvider")
    public void hrun4j_demo_testcase(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
    @Test(dataProvider = "dataProvider")
    public void demo_testcase_request(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}

