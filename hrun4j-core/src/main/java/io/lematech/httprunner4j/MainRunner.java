package io.lematech.httprunner4j;

import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.engine.TestNGEngine;

import java.util.HashSet;
import java.util.Set;


public class MainRunner {
    public static void main(String args[]){
        Set<String> executePaths = new HashSet<>();
        executePaths.add("./hrun4j-core/src/test/resources/testcases");
        RunnerConfig.getInstance().setExecutePaths(executePaths);
        RunnerConfig.getInstance().setRunMode(1);
        TestNGEngine.run();
    }
}
