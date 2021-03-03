package io.lematech.httprunner4j.test;


public class TestCaseExecutorEngine {
    private static TestCaseDefined tdy = null;
    public static synchronized TestCaseDefined getInstance() {
        tdy = new TestCaseDefined();
        return tdy;
    }
}
