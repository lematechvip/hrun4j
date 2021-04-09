package io.lematech.httprunner4j.core.engine;


import io.lematech.httprunner4j.core.runner.TestCaseRunner;

/**
 * @author lematech
 */
public class TestCaseExecutorEngine {
    private static TestCaseRunner tdy = null;
    public static synchronized TestCaseRunner getInstance() {
        tdy = new TestCaseRunner();
        return tdy;
    }
}
