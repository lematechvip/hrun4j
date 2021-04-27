package io.lematech.httprunner4j.junit.converter;


import cn.hutool.core.io.FileUtil;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.converter.ObjectConverter;
import io.lematech.httprunner4j.core.loader.Searcher;
import io.lematech.httprunner4j.core.loader.TestDataLoaderFactory;
import io.lematech.httprunner4j.core.provider.NGDataProvider;
import io.lematech.httprunner4j.core.runner.TestCaseRunner;
import io.lematech.httprunner4j.core.validator.SchemaValidator;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

import java.io.File;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestConverter
 * @description TODO
 * @created 2021/4/6 6:03 下午
 * @publicWechat lematech
 */
public class TestConverter {
    @Test
    public void testApi2TestCase() {
        RunnerConfig.getInstance().setWorkDirectory(new File("/Users/arkhe/Documents/lema/others/httprunner4j/hrun4j-core/src/test/resources"));
        Searcher searcher = new Searcher();
        TestCaseRunner testCaseRunner = new TestCaseRunner();
        String api = "apis/getToken";
        File dataFileResourcePath = searcher.quicklySearchFile(api);
        ApiModel apiModel = TestDataLoaderFactory.getLoader(FileUtil.extName(api)).load(dataFileResourcePath, ApiModel.class);
        SchemaValidator.validateJsonObjectFormat(ApiModel.class, apiModel);
        TestCase testCase = new ObjectConverter().apiModel2TestCase(apiModel);
        testCaseRunner.execute(testCase);
    }

}
