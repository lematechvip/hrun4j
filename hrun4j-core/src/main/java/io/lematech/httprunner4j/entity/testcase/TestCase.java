package io.lematech.httprunner4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

@Data
public class TestCase {
    private Config config;
    @JSONField(name = "teststeps")
    @JsonProperty(value = "teststeps")
    private List<TestStep> testSteps;
}
