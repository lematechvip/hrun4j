package io.lematech.httprunner4j.common;


import io.lematech.httprunner4j.utils.log.MyLog;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DefinedException
 * @description TODO
 * @created 2021/1/21 4:42 下午
 * @publicWechat lematech
 */


public class DefinedException extends RuntimeException{
    public DefinedException(String msg){
        MyLog.error(msg);
    }
}
