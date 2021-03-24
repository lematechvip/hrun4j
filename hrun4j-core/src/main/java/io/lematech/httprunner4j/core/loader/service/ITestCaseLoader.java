package io.lematech.httprunner4j.core.loader.service;


import java.io.File;

public interface ITestCaseLoader {

    /**
     * @param testCaseName
     * @return
     */
    <T> T load(String testCaseName, String extName, Class<T> clazz);

    /**
     * @param fileName
     * @return
     */
    <T> T load(File fileName, Class<T> clazz);

}
