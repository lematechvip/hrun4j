package io.lematech.httprunner4j.handler;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.utils.MyHttpClient;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

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
        Properties props = System.getProperties();
        props.list(System.out);
        System.out.print("-----------------");
        Map map = System.getenv();
        Iterator it = map.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            System.out.print(entry.getKey()+"=");
            System.out.println(entry.getValue());
        }
        handler.load("demo_testcase_request.yml");
    }
    @Test
    public void testExecutor(){
        Executor executor = new Executor();
        executor.execute("hrun4j_demo_testcase.yml");

    }
    @Test
    public  void testHttpClient(){
        System.out.println(JSON.toJSONString(MyHttpClient.doGet("https://postman-echo.com/get")));
    }
}
