package io.lematech.httprunner4j.core.converter;

import cn.hutool.core.bean.BeanUtil;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ObjConverter
 * @description api to testcase converter
 * @created 2021/4/6 5:55 下午
 * @publicWechat lematech
 */
public class ObjectConverter {

    /**
     * api to testcase
     *
     * @param apiModel
     * @return
     */
    public static TestCase api2TestCase(ApiModel apiModel) {
        TestCase testCase = new TestCase();
        TestStep testStep = new TestStep();
        Config config = new Config<>();
        config.setBaseUrl(apiModel.getBaseUrl());
        BeanUtil.copyProperties(apiModel, testStep);
        List<TestStep> testSteps = new ArrayList<>();
        testSteps.add(testStep);
        testCase.setConfig(config);
        testCase.setTestSteps(testSteps);
        return testCase;
    }

}
