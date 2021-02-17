package io.lematech.httprunner4j.base;

import io.lematech.httprunner4j.entity.testcase.TestCase;

public interface ITestCase {
    /**
     * 获取测试用例
     * @param testCaseName
     * @return
     */
    TestCase getTestCase(String testCaseName);
}
