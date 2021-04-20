package io.lematech.httprunner4j.junit.testng;

import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.engine.TestNGEngine;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestNGEngineTest
 * @description TODO
 * @created 2021/4/19 7:47 下午
 * @publicWechat lematech
 */
public class TestNGEngineTest {
    @Test
    public void testEngine() {
        List<File> testCasePaths = new ArrayList<>();
        testCasePaths.add(new File("/Users/arkhe/Documents/lema/others/httprunner4j/hrun4j-core/src/test/resources/testcases"));
        testCasePaths.add(new File("/Users/arkhe/Documents/lema/others/httprunner4j/hrun4j-core/src/test/resources/apis"));
        RunnerConfig.getInstance().setTestCasePaths(testCasePaths);
        TestNGEngine.run();
        Map<String, Set<String>> testCasePackageGroup = TestNGEngine.testCasePkgGroup;
        for (Map.Entry entry : testCasePackageGroup.entrySet()) {
            MyLog.info("包名：{}", entry.getKey());
            MyLog.info("类名：{}", entry.getValue());
        }
    }
}
