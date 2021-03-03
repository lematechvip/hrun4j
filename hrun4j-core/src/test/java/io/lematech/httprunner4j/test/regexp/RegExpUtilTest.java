package io.lematech.httprunner4j.test.regexp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.test.utils.RegExpUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.HashMap;

@Slf4j
public class RegExpUtilTest {
    private RegExpUtil expUtil = new RegExpUtil();
    @Test
    public void testRegexpMethod(){
        String str = "${x}&/api/add=${add(x, y)}&subtract=${subtract(add(x, y),add(x, y))}&multiply=${multiply(x,y)}&divide=${divide(x,y)}&${y}";
        HashMap<String, Object> env = new HashMap<>();
        env.put("x",env);
        env.put("y",1);
        //System.out.println(RegExpUtil.buildNewString(str,env));
       // System.out.println(RegExpUtil.buildNewString("xxxx",env));

      log.info(JSON.toJSONString(env));
        JSONObject object = JSON.parseObject(JSON.toJSONString(env));
        JSONObject newObj = object.fluentRemove("x");
        log.info(JSON.toJSONString(newObj));
    }
}
