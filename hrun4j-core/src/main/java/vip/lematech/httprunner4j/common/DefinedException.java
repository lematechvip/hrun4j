package vip.lematech.httprunner4j.common;


import vip.lematech.httprunner4j.helper.LogHelper;

/**
 * throw defined exception
 * website http://lematech.vip/
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
        LogHelper.error(msg);
    }
}
