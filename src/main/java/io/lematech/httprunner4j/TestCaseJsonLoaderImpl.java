package io.lematech.httprunner4j;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.TestCase;

import java.io.File;
import java.nio.charset.Charset;

public class TestCaseJsonLoaderImpl implements ITestCaseLoader {

    @Override
    public TestCase load(String testCaseName) {
        StringBuffer fileFullName = new StringBuffer();
        fileFullName
                .append(testCaseName)
                .append(".")
                .append(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME);
        return load(new File(fileFullName.toString()));
    }

    @Override
    public TestCase load(File fileName) {
        TestCase testCase ;
        try {
            JSONObject testCaseMetas = JSONUtil.readJSONObject(fileName, Charset.defaultCharset());
            testCase = testCaseMetas.toBean(TestCase.class);
        } catch (Exception e) {
            String exceptionMsg = String.format("file %s not found exception",fileName);
            throw new DefinedException(exceptionMsg);
        }
        return testCase;
    }
}
