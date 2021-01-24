package io.lematech.httprunner4j.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {
    private boolean match(String reg, String str) {
        return Pattern.matches(reg, str);
    }

    public List<String> find(String reg, String str) {
        Matcher matcher = Pattern.compile(reg).matcher(str);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public String findString(String reg, String str) {
        String returnStr = null;
        List<String> list = this.find(reg, str);
        if (list.size() != 0)
            returnStr = list.get(0);
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
