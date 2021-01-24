package io.lematech.httprunner4j.regexp;

import io.lematech.httprunner4j.utils.RegExpUtil;
import org.testng.annotations.Test;

public class RegExpUtilTest {
    private RegExpUtil expUtil = new RegExpUtil();
    @Test
    public void testRegexpMethod(){
        String regExp = "^\\$\\{(\\w+)\\}|\\$(\\w+)$";
        System.out.println(expUtil.find(regExp,"胜多负少的${var}递四方速递"));
    }
}
