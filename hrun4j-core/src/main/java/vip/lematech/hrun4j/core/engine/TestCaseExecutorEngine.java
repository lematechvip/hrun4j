package vip.lematech.hrun4j.core.engine;


import vip.lematech.hrun4j.core.runner.TestCaseRunner;


/**
 * testcase executor engine
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class TestCaseExecutorEngine {
    private static TestCaseRunner tcr = null;
    public static synchronized TestCaseRunner getInstance() {
        tcr = new TestCaseRunner();
        return tcr;
    }
}
