package io.lematech.httprunner4j.test.testng;

import io.lematech.httprunner4j.test.common.Constant;
import io.lematech.httprunner4j.test.config.RunnerConfig;
import io.lematech.httprunner4j.test.TestCaseExecutorEngine;
import io.lematech.httprunner4j.test.base.TestBase;
import io.lematech.httprunner4j.test.entity.testcase.TestCase;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class BuildTestNGAndExec extends TestBase {
    static {
        List<String> executePaths = new ArrayList<>();
        executePaths.add("/Users/arkhe/Documents/lema/others/httprunner4j/src/test/resources/testcases");
        RunnerConfig.getInstance().setExecutePaths(executePaths);
        RunnerConfig.getInstance().setTestCaseExtName(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME);
    }
    @Test(dataProvider = "dataProvider")
    public void buildTestNGAndExe(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    @Test(dataProvider = "dataProvider")
    public void demo_testcase_ref(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
}
