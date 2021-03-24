package io.lematech.httprunner4j.core.loader.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.junit.config.RunnerConfig;
import io.lematech.httprunner4j.core.loader.service.ITestCaseLoader;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;


@Slf4j
public class TestCaseLoaderImpl<T> implements ITestCaseLoader {
    private Yaml yaml;

    public TestCaseLoaderImpl() {
        yaml = new Yaml(new Constructor(JSONObject.class));
    }

    @Override
    public T load(String testCaseName, String extName, Class clazz) {
        List<String> executePaths = RunnerConfig.getInstance().getExecutePaths();
        if (executePaths.size() > 0) {
            return load(new File(testCaseName), clazz);
        } else {
            StringBuffer classResourceTestCasePath = new StringBuffer();
            classResourceTestCasePath.append(testCaseName).append(".");
            classResourceTestCasePath.append(extName);
            log.debug("test case resources path: {}", classResourceTestCasePath.toString());
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(classResourceTestCasePath.toString());
            if (inputStream == null) {
                String exceptionMsg = String.format("in resources the testcase %s is not exists.", classResourceTestCasePath);
                throw new DefinedException(exceptionMsg);
            }
            try {
                if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)) {
                    ObjectMapper mapper = new ObjectMapper();
                    return (T) mapper.readValue(inputStream, clazz);
                } else if (Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
                    JSONObject jsonObject = yaml.load(inputStream);
                    return (T) jsonObject.toJavaObject(clazz);
                }else {
                    String exceptionMsg = String.format("not support %s format,you can implement ITestCaseLoader.java and try override load() method",extName);
                    throw new DefinedException(exceptionMsg);
                }
            }catch (Exception e) {
                String exceptionMsg = String.format("read file %s.%s occur exception: %s",testCaseName,extName,e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
    }

    @Override
    public T load(File fileName, Class clazz) {
        T testCase;
        if (!fileName.exists()) {
            String exceptionMsg = String.format("file %s not found exception", fileName);
            throw new DefinedException(exceptionMsg);
        }
        String extName = RunnerConfig.getInstance().getTestCaseExtName();

        if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                testCase = (T) mapper.readValue(fileName, clazz);
            } catch (Exception e) {
                String exceptionMsg = String.format("read file %s occur exception: %s", fileName, e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        } else if (Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = yaml.load(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                String exceptionMsg = String.format("文件%s不存在", fileName);
                throw new DefinedException(exceptionMsg);
            }
            if (Objects.isNull(jsonObject)) {
                String exceptionMsg = String.format("文件%s内容不能为空", fileName);
                throw new DefinedException(exceptionMsg);
            }
            return (T) jsonObject.toJavaObject(clazz);
        } else {
            String exceptionMsg = String.format("not support %s format,you can implement ITestCaseLoader.java and try override load() method", extName);
            throw new DefinedException(exceptionMsg);
        }
        return testCase;
    }
}
