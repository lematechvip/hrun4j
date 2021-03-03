package io.lematech.httprunner4j.test;

import io.lematech.httprunner4j.test.config.RunnerConfig;

import java.util.ArrayList;
import java.util.List;

public class MainRunner {
    public static void main(String args[]){
        List<String> executePaths = new ArrayList<>();
        executePaths.add("./src/test/resources/testcases");
        RunnerConfig.getInstance().setExecutePaths(executePaths);
        TestNGEngine.run();
    }
}
