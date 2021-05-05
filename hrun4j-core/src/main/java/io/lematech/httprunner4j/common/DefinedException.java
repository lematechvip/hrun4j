package io.lematech.httprunner4j.common;


import io.lematech.httprunner4j.widget.log.MyLog;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DefinedException
 * @description throw defined exception
 * @created 2021/1/21 4:42 下午
 * @publicWechat lematech
 */


public class DefinedException extends RuntimeException {
    /**
     * define exception
     *
     * @param msg
     */
    public DefinedException(String msg) {
        MyLog.error(msg);
    }
}
