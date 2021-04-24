package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.loader.impl.TestDataLoaderImpl;
import io.lematech.httprunner4j.core.loader.service.ITestDataLoader;
import io.lematech.httprunner4j.widget.log.MyLog;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestDataLoaderFactory
 * @description Test data loader factory by test case extname
 * @created 2021/4/19 4:12 下午
 * @publicWechat lematech
 */

public class TestDataLoaderFactory {
    /**
     * get file loader
     *
     * @param extName
     * @return
     */
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
