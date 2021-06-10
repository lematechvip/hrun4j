package vip.lematech.httprunner4j.core.loader.service;


import java.io.File;


/**
 * Data loading define class, support file loading
 *
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public interface ITestDataLoader {


    /**
     * file load
     *
     * @param fileName The file name
     * @param clazz    The specified class
     * @param <T>      The generic type
     * @return The specified object
     */
    <T> T load(File fileName, Class<T> clazz);

}
