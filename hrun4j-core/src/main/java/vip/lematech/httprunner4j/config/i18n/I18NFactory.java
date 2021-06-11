package vip.lematech.httprunner4j.config.i18n;

import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.config.RunnerConfig;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class I18NFactory {
    private synchronized static ResourceBundle getBundle(String locale) {
        Locale localeLang;
        if (Constant.I18N_US.equalsIgnoreCase(locale)) {
            localeLang = new Locale("en", "US");
        } else if (Constant.I18N_CN.equalsIgnoreCase(locale)) {
            localeLang = new Locale("zh", "CN");
        } else {
            localeLang = new Locale("zh", "CN");
        }
        return ResourceBundle.getBundle("locales.message", localeLang);
    }
    public synchronized static String getLocaleMessage(String key) {
        return getBundle(RunnerConfig.getInstance().getI18n()).getString(key);
    }
}
