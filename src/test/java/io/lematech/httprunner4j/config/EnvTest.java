package io.lematech.httprunner4j.config;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className EnvTest
 * @description TODO
 * @created 2021/1/22 3:55 下午
 * @publicWechat lematech
 */
@Slf4j
public class EnvTest {
    @Test
    public void testYamlLoad(){
        Env.setEnv("user","arkhe");
        log.info(Env.getEnv("user"));
        log.info(Env.getEnv("prod"));
    }
}
