package io.lematech.httprunner4j;

import io.lematech.httprunner4j.common.Constant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RunnerConfig {
    public List<String> getExecutePaths() {
        if(executePaths.isEmpty()){
            executePaths.add(".");
        }
        return executePaths;
    }

    private List<String> executePaths;
    private String testCaseExtName;
    private static RunnerConfig instance = new RunnerConfig();
    private RunnerConfig() {
        executePaths = new ArrayList<>();
        testCaseExtName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;
    }
    public static RunnerConfig getInstance(){
        return instance;
    }

}
