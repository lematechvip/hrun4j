package io.lematech.httprunner4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestCase
 * @description TODO
 * @created 2021/1/20 2:39 下午
 * @publicWechat lematech
 */

public class TestCase {
    private Config config;
    @JSONField(name="teststeps")
    private List<TestStep> testSteps;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<TestStep> testSteps) {
        this.testSteps = testSteps;
    }
}
