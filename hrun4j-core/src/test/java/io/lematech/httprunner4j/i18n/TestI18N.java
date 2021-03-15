package io.lematech.httprunner4j.i18n;

import io.lematech.httprunner4j.I18NFactory;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.config.RunnerConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestI18N
 * @description TODO
 * @created 2021/3/15 3:56 下午
 * @publicWechat lematech
 */
@Slf4j
public class TestI18N {
    @Test
    public void testI18N() {
        RunnerConfig.getInstance().setI18n(Constant.I18N_US);
        log.info(I18NFactory.getLocaleMessage("helloWorld"));
        log.info(String.format(I18NFactory.getLocaleMessage("time"), "08:00"));
    }
}
