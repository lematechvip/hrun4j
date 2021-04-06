package io.lematech.httprunner4j.junit.converter;


import cn.hutool.core.io.FileUtil;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.converter.ObjectConverter;
import io.lematech.httprunner4j.core.loader.TestCaseLoaderFactory;
import io.lematech.httprunner4j.core.provider.NGDataProvider;
import io.lematech.httprunner4j.core.runner.TestCaseRunner;
import io.lematech.httprunner4j.core.validator.SchemaValidator;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

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
        NGDataProvider ngDataProvider = new NGDataProvider();
        TestCaseRunner testCaseRunner = new TestCaseRunner();
        String api = "apimodel/getToken";
        String dataFileResourcePath = ngDataProvider.seekModelFileByCasePath(api);
        ApiModel apiModel = TestCaseLoaderFactory.getLoader(FileUtil.extName(api)).load(dataFileResourcePath, RunnerConfig.getInstance().getTestCaseExtName(), ApiModel.class);
        SchemaValidator.validateJsonObjectFormat(ApiModel.class, apiModel);
        TestCase testCase = ObjectConverter.api2TestCase(apiModel);
        testCaseRunner.execute(testCase);
    }

}
