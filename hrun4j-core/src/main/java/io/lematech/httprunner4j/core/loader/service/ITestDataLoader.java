package io.lematech.httprunner4j.core.loader.service;


import java.io.File;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestDataLoaderImpl
 * @description Data loading define class, support file loading
 * @created 2021/1/20 4:50 下午
 * @publicWechat lematech
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
