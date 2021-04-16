package io.lematech.httprunner4j.widget.utils;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
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

public class RegExpUtil {


    public static Boolean isExp(String exp) {
        Boolean flag = false;
        if (StringUtils.isEmpty(exp)) {
            return false;
        }
        if (match(Constant.REGEX_EXPRESSION_FLAG, exp)) {
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
        if (StrUtil.isEmpty(reg)) {
            String exceptionMsg = String.format("regex expresssion can not empty or null: %s", reg);
            throw new DefinedException(exceptionMsg);
        }
        if (StrUtil.isEmpty(str)) {
            String exceptionMsg = String.format("search str can not empty or null: %s", str);
            throw new DefinedException(exceptionMsg);
        }
        String returnStr = null;
        List<String> list = find(reg, str);
        if (list.size() != 0) {
            returnStr = list.get(0);
        }
        return returnStr;
    }

    public static boolean isUrl(String s) {
        try {
            Pattern patt = Pattern.compile(Constant.URL_REGEX);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }

    }
}
