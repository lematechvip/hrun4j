package io.lematech.httprunner4j.test;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.test.common.Constant;
import io.lematech.httprunner4j.test.common.DefinedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestCaseLoaderFactory {
    public synchronized static ITestCaseLoader getLoader(String extName){
        if(StrUtil.isEmpty(extName)){
            log.info("default set testcase loader is yml");
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
