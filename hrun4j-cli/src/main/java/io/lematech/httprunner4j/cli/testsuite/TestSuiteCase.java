package io.lematech.httprunner4j.cli.testsuite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestSuiteCase
 * @description TODO
 * @created 2021/5/12 4:17 下午
 * @publicWechat lematech
 */
@Data
public class TestSuiteCase<T> {
    private String name;
    @JsonProperty(value = "variables")
    private T variables;
    private T parameters;
    @JsonProperty(value = "testcase")
    private String caseRelativePath;
}
