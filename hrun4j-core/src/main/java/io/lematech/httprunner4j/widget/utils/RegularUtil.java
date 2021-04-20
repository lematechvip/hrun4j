package io.lematech.httprunner4j.widget.utils;


import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RegularUtil
 * @description TODO
 * @created 2021/3/16 6:00 下午
 * @publicWechat lematech
 */
public class RegularUtil {
    /**
     * 路径转包名
     *
     * @param dirPath
     * @return
     */
    public static String dirPath2pkgName(String dirPath) {
        StringBuffer pkgName = new StringBuffer();
        if (StrUtil.isEmpty(dirPath)) {
            return null;
        }
        if (dirPath.startsWith(Constant.DOT_PATH)) {
            dirPath = dirPath.replaceFirst("\\.", "");
        }
        pkgName.append(dirPath.replaceAll("/", Constant.DOT_PATH));
        return pkgName.toString();
    }


    /**
     * @param pkgPath
     * @return
     */
    public static String pkgPath2DirPath(String pkgPath) {
        if (StrUtil.isEmpty(pkgPath)) {
            return null;
        }
        return pkgPath.replaceAll("\\.", "/");
    }

    /**
     * 根据正则替换最后一个符合条件的字符
     *
     * @param text
     * @param regex
     * @param replacement
     * @return
     */
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }


    /**
     * 毫秒转秒
     *
     * @return
     */
    public static Integer s2ms(Integer s) {
        if (Objects.isNull(s)) {
            return null;
        }
        Integer ms;
        try {
            ms = s * 1000;
        } catch (Exception e) {
            String exceptionMsg = String.format("ms to s occur exception：%s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return ms;
    }

    public static String getSimpleDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String formatJsonOutput(Object output) {
        if (Objects.isNull(output)) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        } catch (JsonProcessingException e) {
            return String.valueOf(output);
        }
    }
}
