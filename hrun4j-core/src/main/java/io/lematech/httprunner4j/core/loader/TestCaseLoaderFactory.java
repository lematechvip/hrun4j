package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.loader.impl.TestCaseLoaderImpl;
import io.lematech.httprunner4j.core.loader.service.ITestCaseLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestCaseLoaderFactory {
    public synchronized static ITestCaseLoader getLoader(String extName){
        if(StrUtil.isEmpty(extName)){
            log.debug("default set testcase loader is yml");
            return new TestCaseLoaderImpl();
        }
        if(extName.equalsIgnoreCase(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME)
                ||extName.equalsIgnoreCase(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME)){
            return new TestCaseLoaderImpl();
        }else{
            String exceptionMsg = String.format("ext name %s not support.",extName);
            throw new DefinedException(exceptionMsg);
        }
    }
}
