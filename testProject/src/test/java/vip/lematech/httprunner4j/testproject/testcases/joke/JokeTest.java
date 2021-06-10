package vip.lematech.httprunner4j.testproject.testcases.joke;

import vip.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.httprunner4j.entity.testcase.TestCase;
import vip.lematech.httprunner4j.testproject.HttpRunner4j;

import org.testng.annotations.Test;

/**
* @author lematech@foxmail.com
* @version 1.0.0
* @className JokeTest
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
