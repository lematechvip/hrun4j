package io.lematech.httprunner4j.entity.testcase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestSuite
 * @description TestSuite
 * @created 2021/4/25 6:42 下午
 * @publicWechat lematech
 */
@Data
public class TestSuite {
    private Config config;
    @JsonProperty(value = "testcases")
    private List<TestSuiteCase> testCases;
}
