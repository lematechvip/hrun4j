package io.lematech.httprunner4j.entity.testcase;

import lombok.Data;

@Data
public class TestCaseMeta {
    private TestCase testCase;
    private String methodName;
}
