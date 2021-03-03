package io.lematech.httprunner4j.test.utils;

import io.lematech.httprunner4j.test.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RegExpUtil
 * @description TODO
 * @created 2021/1/22 4:07 下午
 * @publicWechat lematech
 */

@Slf4j
public class RegExpUtil {
    public static String buildNewString(String str, Map env){
        if(isExp(str)){
            List<String> matchList = RegExpUtil.find(Constant.REGEX_EXPRESSION,str);
            List<String> matcherList = new ArrayList<>();
            for(String exp : matchList){
                String handleResult = String.valueOf(AviatorEvaluatorUtil.execute(exp,env));
                matcherList.add(handleResult);
            }
            for(String match : matcherList){
                str = str.replaceFirst(Constant.REGEX_EXPRESSION_REPLACE,match);
            }
        }
        return str;
    }
    public static Boolean isExp(String exp){
        Boolean flag = false;
        if(StringUtils.isEmpty(exp)){
            return false;
        }
        if(match(Constant.REGEX_EXPRESSION_FLAG,exp)){
            flag = true;
        }
        return flag;
    }

    private static boolean match(String reg, String str) {
        return Pattern.matches(reg, str);
    }

    public static List<String> find(String reg, String str) {
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static String findString(String reg, String str) {
        String returnStr = null;
        List<String> list = find(reg, str);
        if (list.size() != 0){
            returnStr = list.get(0);
        }
        return returnStr;
    }
}
