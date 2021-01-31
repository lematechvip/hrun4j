package io.lematech.httprunner4j.regexp;

import io.lematech.httprunner4j.utils.RegExpUtil;
import org.testng.annotations.Test;

import java.util.HashMap;

public class RegExpUtilTest {
    private RegExpUtil expUtil = new RegExpUtil();
    @Test
    public void testRegexpMethod(){
        String str = "${x}&/api/add=${add(x, y)}&subtract=${subtract(add(x, y),add(x, y))}&multiply=${multiply(x,y)}&divide=${divide(x,y)}&${y}";
        HashMap<String, Object> env = new HashMap<>();
        env.put("x",2);
        env.put("y",1);
        System.out.println(RegExpUtil.buildNewString(str,env));
        System.out.println(RegExpUtil.buildNewString("xxxx",env));
    }
}
