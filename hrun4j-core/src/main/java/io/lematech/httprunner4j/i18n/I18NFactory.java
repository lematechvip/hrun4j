package io.lematech.httprunner4j.i18n;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.config.RunnerConfig;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className I18NFactory
 * @description TODO
 * @created 2021/3/15 3:40 下午
 * @publicWechat lematech
 */
public class I18NFactory {
    private synchronized static ResourceBundle getBundle(String locale) {
        if (StrUtil.isEmpty(locale) || locale.equalsIgnoreCase(Constant.I18N_CN)) {
            Locale localeZh = new Locale("zh", "CN");
            return ResourceBundle.getBundle("locales.message", localeZh);
        } else {
            Locale localeEn = new Locale("en", "US");
            return ResourceBundle.getBundle("locales.message", localeEn);
        }
    }
    public synchronized static String getLocaleMessage(String key) {
        return getBundle(RunnerConfig.getInstance().getI18n()).getString(key);
    }
}
