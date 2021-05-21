package io.lematech.httprunner4j.core.loader.service;


import java.io.File;


/**
 * Data loading define class, support file loading
 *
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public interface ITestDataLoader {


    /**
     * file load
     *
     * @param fileName
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T load(File fileName, Class<T> clazz);

}
