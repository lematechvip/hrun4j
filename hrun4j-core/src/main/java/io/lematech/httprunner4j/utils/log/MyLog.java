package io.lematech.httprunner4j.utils.log;


import cn.hutool.core.text.StrFormatter;
import io.lematech.httprunner4j.utils.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className MyLog
 * @description TODO
 * @created 2021/4/9 3:20 下午
 * @publicWechat lematech
 */
@Slf4j
public class MyLog {
    /**
     * format output
     *
     * @param logStr
     * @param args
     * @return
     */
    private static String formatInfo(String logStr, Object... args) {
        return StrFormatter.format(logStr, args);
    }

    public static void info(String logStr, Object... args) {
        log.info(formatInfo(logStr, args));
        Reporter.log("[" + RegularUtil.getSimpleDateFormat() + "] " + formatInfo(logStr, args));
    }

    /**
     * error log output and reporter output
     *
     * @param logStr
     * @param args
     */
    public static void error(String logStr, Object... args) {
        log.error(formatInfo(logStr, args));
        Reporter.log("[" + RegularUtil.getSimpleDateFormat() + "] " + formatInfo(logStr, args));
    }

    public static void debug(String logStr, Object... args) {
        log.debug(formatInfo(logStr, args));
    }

    public static void warn(String logStr, Object... args) {
        log.warn(formatInfo(logStr, args));
        Reporter.log("[" + RegularUtil.getSimpleDateFormat() + "] " + formatInfo(logStr, args));
    }
}
