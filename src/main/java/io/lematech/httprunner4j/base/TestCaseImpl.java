package io.lematech.httprunner4j.base;

import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.handler.Handler;

public class TestCaseImpl implements ITestCase{
    private Handler handler ;
    public TestCaseImpl(){
        this.handler = new Handler();
    }
    @Override
    public TestCase getTestCase(String testCaseName) {
        return handler.load(testCaseName);
    }
}
