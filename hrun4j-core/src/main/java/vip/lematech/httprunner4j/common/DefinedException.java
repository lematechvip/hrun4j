package vip.lematech.httprunner4j.common;


import vip.lematech.httprunner4j.widget.log.MyLog;

/**
 * throw defined exception
 *
 * @author lematech@foxmail.com
 * @version 1.0.0
 */


public class DefinedException extends RuntimeException {
    /**
     * define exception
     *
     * @param msg exception message
     */
    public DefinedException(String msg) {
        MyLog.error(msg);
    }
}