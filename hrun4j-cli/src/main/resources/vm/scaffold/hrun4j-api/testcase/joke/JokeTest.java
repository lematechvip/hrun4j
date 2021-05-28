package io.lematech.firstproject.testcases.joke;

import io.lematech.firstproject.testcases.HttpRunner4j;
import io.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JokeTest
 * @description TODO
 * @created 2021/5/25 9:27 上午
 * @publicWechat lematech
 */
public class JokeTest extends HttpRunner4j
{
    /**
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void lookTheJokeFromJokeList(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

}
