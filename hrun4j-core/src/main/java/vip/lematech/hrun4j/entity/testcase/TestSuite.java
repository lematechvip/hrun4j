package vip.lematech.hrun4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Test Case Suite
 *
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Data
public class TestSuite {
    private Config config;
    @JsonProperty(value = "testcases")
    @JSONField(name = "testcases")
    private List<TestSuiteCase> testCases;
}
