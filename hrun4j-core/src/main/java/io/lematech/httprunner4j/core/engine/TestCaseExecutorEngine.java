package io.lematech.httprunner4j.core.engine;


import io.lematech.httprunner4j.core.runner.TestCaseDefined;

/**
 * @author lematech
 */
public class TestCaseExecutorEngine {
    private static TestCaseDefined tdy = null;

    public static synchronized TestCaseDefined getInstance() {
        tdy = new TestCaseDefined();
        return tdy;
    }
}
