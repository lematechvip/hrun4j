package io.lematech.httprunner4j;

import io.lematech.httprunner4j.control.TestCaseDefined;

public class TestCaseExecutorEngine {
    private static TestCaseDefined tdy = null;
    public static synchronized TestCaseDefined getInstance() {
        tdy = new TestCaseDefined();
        return tdy;
    }
}
