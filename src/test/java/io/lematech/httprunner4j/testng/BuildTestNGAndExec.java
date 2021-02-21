package io.lematech.httprunner4j.testng;

import io.lematech.httprunner4j.TestCaseExecutorEngine;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

public class BuildTestNGAndExec extends TestBase {
    @Test(dataProvider = "dataProvider")
    public void buildTestNGAndExe(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
