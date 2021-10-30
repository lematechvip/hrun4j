package vip.lematech.hrun4j.helper;

import cn.hutool.core.util.StrUtil;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class RegExpHelper {

    /**
     * is exp
     *
     * @param exp The expression
     * @return true or false
     */
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

    /**
     * Is parameterize exp
     * @param exp expression
     * @return true or false
     */
    public static Boolean isParameterizeExp(String exp) {
        Boolean flag = false;
        if (StringUtils.isEmpty(exp)) {
            return false;
        }
        if (match(Constant.REGEX_PARAMETERIZE_EXPRESSION_FLAG, exp)) {
            flag = true;
        }
        return flag;
    }

    /**
     * @param reg
     * @param str
     * @return
     */
    private static boolean match(String reg, String str) {
        return Pattern.matches(reg, str);
    }

    /**
     * @param reg regex pattern
     * @param str string
     * @return find result
     */
    public static List<String> find(String reg, String str) {
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * According to the regular search to meet the requirements
     *
     * @param reg regular expression
     * @param str The character to be found
     * @return find result
     */
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

    /**
     * is url
     * @param s url
     * @return true if url,otherwise false
     */
    public static boolean isUrl(String s) {
        Matcher matcher = Pattern.compile(Constant.URL_REGEX).matcher(s);
        return matcher.matches();
    }
}
