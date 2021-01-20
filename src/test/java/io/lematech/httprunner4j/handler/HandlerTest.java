package io.lematech.httprunner4j.handler;

import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className HandlerTest
 * @description TODO
 * @created 2021/1/20 6:45 下午
 * @publicWechat lematech
 */

public class HandlerTest {
    private Handler handler = new Handler();
    @Test
    public void testYamlLoad(){
        handler.loadFile("testcase");
    }
}
