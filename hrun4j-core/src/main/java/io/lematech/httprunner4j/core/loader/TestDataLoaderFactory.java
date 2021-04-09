package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.loader.impl.TestDataLoaderImpl;
import io.lematech.httprunner4j.core.loader.service.ITestDataLoader;
import io.lematech.httprunner4j.utils.log.MyLog;
import lombok.extern.slf4j.Slf4j;

public class TestDataLoaderFactory {
    public synchronized static ITestDataLoader getLoader(String extName) {
        if (StrUtil.isEmpty(extName)) {
            MyLog.debug("default set testcase loader is yml");
            return new TestDataLoaderImpl();
        }
        if (extName.equalsIgnoreCase(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME)
                || extName.equalsIgnoreCase(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME)) {
            return new TestDataLoaderImpl();
        } else {
            String exceptionMsg = String.format("ext name %s not support.", extName);
            throw new DefinedException(exceptionMsg);
        }
    }
}
