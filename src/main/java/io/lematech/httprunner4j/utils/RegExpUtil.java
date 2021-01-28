package io.lematech.httprunner4j.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RegExpUtil {
    public static String buildNewString(String str, Map env){
        String regExpExp = "(.*)\\$\\{(.*?)\\}(.*)";
        if(match(regExpExp,str)){
            String regExp = "(?<=\\$\\{).*?(?=})";
            List<String> matchList = RegExpUtil.find(regExp,str);
            List<String> matcherList = new ArrayList<>();
            for(String exp : matchList){
                String handleResult = String.valueOf(AviatorEvaluatorUtil.execute(exp,env));
                matcherList.add(handleResult);
            }
            String regExpReplace = "\\$\\{.*?}";
            for(String match : matcherList){
                str = str.replaceFirst(regExpReplace,match);
            }
        }
        return str;
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




    /*public boolean isTransform(String str) {
        return this.match(".*" + Keyword.getKeyword("random") + ".*", str);
    }

    public String replaceRandom(String str) {
        String findStr = this.findString(Keyword.getKeyword("random"), str);
        int randomNum = Integer.parseInt(this.findString("(?<=\\{).*(?=\\})",
                str));
        String findStr_ = findStr.replaceFirst(Keyword.getKeyword("random"),
                new TimeString().getRandomNum(randomNum));
        return str.replace(findStr, findStr_);
    }*/
}
