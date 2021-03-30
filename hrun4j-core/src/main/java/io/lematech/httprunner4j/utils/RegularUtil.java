package io.lematech.httprunner4j.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;

import java.util.List;
import java.util.Map;
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
     * @param pkgPath
     * @return
     */
    public static String dirPath2pkgName(String pkgPath) {
        StringBuffer pkgName = new StringBuffer();
        if (StrUtil.isEmpty(pkgPath)) {
            return pkgName.toString();
        }
        if (pkgPath.startsWith(Constant.DOT_PATH)) {
            pkgPath = pkgPath.replaceFirst("\\.", "");
        }
        pkgName.append(pkgPath.replaceAll("/", Constant.DOT_PATH));
        return pkgName.toString();
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
}
