package io.lematech.httprunner4j;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.utils.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class NGDataProvider {

    public static Object[][] dataProvider(String pkgName, String testCaseName) {
        String extName = RunnerConfig.getInstance().getTestCaseExtName();
        String dataFileResourcePath = seekDataFileByRule(pkgName, testCaseName, extName);
        TestCase testCase = TestCaseLoaderFactory.getLoader(extName)
                .load(dataFileResourcePath, extName, TestCase.class);
        SchemaValidator.validateTestCaseValid(testCase);
        Object[][] testCases = getObjects(testCase);
        return testCases;
    }

    private static Object[][] getObjects(TestCase testCase) {
        Object[][] testCases;
        List<TestCase> result = handleMultiGroupData(testCase);
        testCases = new Object[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            testCases[i] = new Object[]{result.get(i)};
        }
        return testCases;
    }

    public static String seekModelFileByCasePath(String filePath) {
        if (!StrUtil.isEmpty(filePath)) {
            if (filePath.startsWith(Constant.TEST_CASE_FILE_PATH)) {
                filePath = filePath.replaceFirst(Constant.TEST_CASE_FILE_PATH, "");
            } else if (filePath.startsWith(Constant.TEST_CASE_DIRECTORY_NAME)) {
                filePath = filePath.replaceFirst(Constant.TEST_CASE_DIRECTORY_NAME, "");
            }
            String extName = FileUtil.extName(filePath);
            String mainName = FileUtil.mainName(filePath);
            if (StrUtil.isEmpty(extName)) {
                extName = RunnerConfig.getInstance().getTestCaseExtName();
            } else {
                filePath = RegularUtil.replaceLast(filePath, mainName + Constant.DOT_PATH + extName, "");
                if (filePath.endsWith("/")) {
                    filePath = RegularUtil.replaceLast(filePath, "/", "");
                }
            }
            log.info("路径名：{},文件名：{}，扩展名：{}", RegularUtil.dirPath2pkgName(filePath), mainName, extName);
            return seekDataFileByRule(RegularUtil.dirPath2pkgName(filePath), mainName, extName);
        }
        return "";
    }

    private static String seekDataFileByRule(String pkgName, String testCaseName, String extName) {
        List<String> executePaths = RunnerConfig.getInstance().getExecutePaths();
        if (executePaths.size() > 0) {
            for (String path : executePaths) {
                searchTestCaseByName(path, testCaseName);
                if (!StrUtil.isEmpty(testCasePath)) {
                    break;
                }
            }
            if (StrUtil.isEmpty(testCasePath)) {
                String exceptionMsg = String.format("in %s path,not found  %s.%s",executePaths,testCaseName,extName);
                throw new DefinedException(exceptionMsg);
            }
            return testCasePath;
        }

        StringBuffer dataFileResourcePath = new StringBuffer();
        dataFileResourcePath.append(Constant.TEST_CASE_DIRECTORY_NAME).append(File.separator);
        if(!StrUtil.isEmpty(pkgName)){
            String[] pkgNameMetas = pkgName.split("\\.");
            int pkgNameMetaLength = pkgNameMetas.length;
            if (pkgNameMetaLength >= 2) {
                log.debug("full package: {},company type,company name: {} project name: {}", pkgName, pkgNameMetas[0], pkgNameMetas[1], pkgNameMetas[2]);
            }
            for(int index = 3;index< pkgNameMetaLength; index++) {
                dataFileResourcePath.append(pkgNameMetas[index]).append(File.separator);
            }
        }
        dataFileResourcePath.append(testCaseName);
        return dataFileResourcePath.toString();
    }
    private static String testCasePath ;

    /**
     * @param path
     * @param testCaseName
     * @return
     */
    private static String searchTestCaseByName(String path, String testCaseName) {
        File filesPath = new File(path);
        if (!filesPath.exists()) {
            String exceptionMsg = String.format("file %s is not exits", filesPath.getAbsolutePath());
            throw new DefinedException(exceptionMsg);
        }
        File[] files = filesPath.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                StringBuffer testCaseFullName = new StringBuffer();
                testCaseFullName.append(testCaseName).append(Constant.DOT_PATH)
                        .append(RunnerConfig.getInstance().getTestCaseExtName());
                if (file.exists() && file.getName().equalsIgnoreCase(testCaseFullName.toString())) {
                    log.debug("filename {}", file.getName());
                    log.debug("testCaseFullName:{}", testCaseFullName.toString().trim());
                    log.debug("file Path {}", file.getPath());
                    testCasePath = file.getPath();
                    return testCasePath;
                }
            }else {
                searchTestCaseByName(file.getPath(),testCaseName);
            }
        }
        return "";
    }


    /**
     * user_id: [1001, 1002, 1003, 1004]
     * username-password:
     * - ["user1", "111111"]
     * - ["user2", "222222"]
     * - ["user3", "333333"]
     */
    private static List<TestCase> handleMultiGroupData(TestCase testCase) {
        ArrayList<TestCase> result = new ArrayList<>();
        Object parameters = testCase.getConfig().getParameters();
        if (parameters == null) {
            result.add(testCase);
            return result;
        }
        if(parameters instanceof Map){
            parameters = JSONObject.parseObject(JSON.toJSONString(parameters));
        }

        log.info("class:{}",parameters.getClass());
        if (parameters instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) parameters;
            for (Map.Entry entry : jsonObject.entrySet()) {
                String key = (String) entry.getKey();
                String[] params = key.split("-");
                Object value = entry.getValue();
                if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    for (int i = 0; i < array.size(); i++) {
                        Object arr = array.get(i);
                        ObjectMapper objectMapper = new ObjectMapper();
                        TestCase cpTestCase;
                        try {
                            cpTestCase = objectMapper.readValue(objectMapper.writeValueAsString(testCase), TestCase.class);
                        } catch (JsonProcessingException e) {
                            String exceptionMsg = String.format("testcase deep copy exception : %s", e.getMessage());
                            throw new DefinedException(exceptionMsg);
                        }
                        Map<String, Object> configVariables = (Map) cpTestCase.getConfig().getVariables();
                        Map parameterVariables = Maps.newHashMap();
                        Map resultVariables = Maps.newHashMap();
                        if (params.length == 1) {
                            String name = params[0];
                            parameterVariables.put(name, arr);
                        } else {
                            if (arr instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) arr;
                                int size = jsonArray.size();
                                for (int index = 0; index < size; index++) {
                                    String name = params[index];
                                    parameterVariables.put(name, jsonArray.get(index));
                                }
                            }
                        }
                        resultVariables.putAll(configVariables);
                        resultVariables.putAll(parameterVariables);
                        Config config = cpTestCase.getConfig();
                        config.setVariables(resultVariables);
                        config.setParameters(parameterVariables);
                        cpTestCase.setConfig(config);
                        result.add(cpTestCase);
                    }
                }
            }
        }
        return result;
    }
}
