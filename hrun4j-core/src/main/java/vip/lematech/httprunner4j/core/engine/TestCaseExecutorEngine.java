package vip.lematech.httprunner4j.core.engine;


import vip.lematech.httprunner4j.core.runner.TestCaseRunner;


/**
 * testcase executor engine
 *
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public class TestCaseExecutorEngine {
    private static TestCaseRunner tcr = null;
    public static synchronized TestCaseRunner getInstance() {
        tcr = new TestCaseRunner();
        return tcr;
    }
}
