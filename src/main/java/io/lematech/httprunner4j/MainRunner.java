package io.lematech.httprunner4j;

import io.lematech.httprunner4j.common.Constant;

public class MainRunner {
    public static void main(String args[]){
        TestNGEngine.run(Constant.TEST_CASE_FILE_PATH);
    }
}
