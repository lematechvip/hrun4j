package vip.lematech.hrun4j.helper;


import cn.hutool.core.text.StrFormatter;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

/**
 * logger output
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Slf4j
public class LogHelper {
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
        Reporter.log("[" + LittleHelper.getSimpleDateFormat() + "] " + formatInfo(logStr, args));
    }

    /**
     * error log output and reporter output
     *
     * @param logStr logger output
     * @param args arguments
     */
    public static void error(String logStr, Object... args) {
        log.error(formatInfo(logStr, args));
        Reporter.log("[" + LittleHelper.getSimpleDateFormat() + "] " + formatInfo(logStr, args));
    }

    public static void debug(String logStr, Object... args) {
        log.debug(formatInfo(logStr, args));
    }

    public static void warn(String logStr, Object... args) {
        log.warn(formatInfo(logStr, args));
        Reporter.log("[" + LittleHelper.getSimpleDateFormat() + "] " + formatInfo(logStr, args));
    }
}
