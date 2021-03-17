package io.lematech.httprunner4j.utils;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RegularUtil
 * @description TODO
 * @created 2021/3/16 6:00 下午
 * @publicWechat lematech
 */
public class RegularUtil {
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

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }
}
