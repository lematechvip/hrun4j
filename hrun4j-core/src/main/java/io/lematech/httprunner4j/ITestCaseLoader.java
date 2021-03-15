package io.lematech.httprunner4j;

import io.lematech.httprunner4j.entity.testcase.TestCase;

import java.io.File;

public interface ITestCaseLoader {

    /**
     * @param testCaseName
     * @return
     */
    TestCase load(String testCaseName, String extName);

    /**
     *
     * @param fileName
     * @return
     */
    TestCase load(File fileName);
}
