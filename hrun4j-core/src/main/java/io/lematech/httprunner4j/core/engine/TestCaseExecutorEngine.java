package io.lematech.httprunner4j.core.engine;


import io.lematech.httprunner4j.core.runner.TestCaseRunner;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestCaseExecutorEngine
 * @description testcase executor engine
 * @created 2021/4/6 11:20 下午
 * @publicWechat lematech
 */

public class TestCaseExecutorEngine {
    private static TestCaseRunner tdy = null;
    public static synchronized TestCaseRunner getInstance() {
        tdy = new TestCaseRunner();
        return tdy;
    }
}
