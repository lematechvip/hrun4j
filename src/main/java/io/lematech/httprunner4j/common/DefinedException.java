package io.lematech.httprunner4j.common;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DefinedException
 * @description TODO
 * @created 2021/1/21 4:42 下午
 * @publicWechat lematech
 */

@Slf4j
public class DefinedException extends RuntimeException{
    public DefinedException(String msg){
        if(StrUtil.isEmpty(msg)){
            log.info("空空");
        }
        log.error(msg);
    }
}
