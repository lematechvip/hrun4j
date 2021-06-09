package vip.lematech.httprunner4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Test Case Suite
 *
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
@Data
public class TestSuite {
    private Config config;
    @JsonProperty(value = "testcases")
    @JSONField(name = "testcases")
    private List<TestSuiteCase> testCases;
}
