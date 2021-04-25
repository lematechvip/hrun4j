package io.lematech.httprunner4j.entity.testcase;

import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestSuiteCase
 * @description testsuite testcase
 * @created 2021/4/25 8:03 下午
 * @publicWechat lematech
 */
@Data
public class TestSuiteCase {
    private String name;
    /**
     * "testcase reference, value is testcase file relative path
     */
    private String testcase;

}
