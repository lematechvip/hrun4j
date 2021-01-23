package io.lematech.httprunner4j.common;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DefinedException
 * @description TODO
 * @created 2021/1/21 4:42 下午
 * @publicWechat lematech
 */
public class DefinedException extends RuntimeException{
    private String msg;
    public DefinedException(String msg){
        this.msg = msg;
    }
}
