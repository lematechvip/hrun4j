package vip.lematech.httprunner4j.core.loader;

import cn.hutool.core.util.StrUtil;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.common.DefinedException;
import vip.lematech.httprunner4j.core.loader.impl.TestDataLoaderImpl;
import vip.lematech.httprunner4j.core.loader.service.ITestDataLoader;
import vip.lematech.httprunner4j.widget.log.MyLog;


/**
 * Test data loader factory by test case extname
 *
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public class TestDataLoaderFactory {
    /**
     * get file loader
     * @param extName extension name
     * @return test data loader
     */
    public synchronized static ITestDataLoader getLoader(String extName) {
        if (StrUtil.isEmpty(extName)) {
            MyLog.debug("Set the use case load format to YML by default");
            return new TestDataLoaderImpl(extName);
        }
        if (extName.equalsIgnoreCase(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME)
                || extName.equalsIgnoreCase(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME)) {
            return new TestDataLoaderImpl(extName);
        } else {
            String exceptionMsg = String.format("The current extension %s is not supported.", extName);
            throw new DefinedException(exceptionMsg);
        }
    }
}
