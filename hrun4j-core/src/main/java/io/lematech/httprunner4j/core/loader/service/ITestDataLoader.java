package io.lematech.httprunner4j.core.loader.service;


import java.io.File;

public interface ITestDataLoader {


    /**
     * @param fileName
     * @return
     */
    <T> T load(File fileName, Class<T> clazz);

}
