package io.lematech.httprunner4j.core.loader.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.converter.ObjectConverter;
import io.lematech.httprunner4j.core.loader.service.ITestDataLoader;
import io.lematech.httprunner4j.core.validator.SchemaValidator;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.utils.log.MyLog;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;


public class TestDataLoaderImpl<T> implements ITestDataLoader {
    private Yaml yaml;
    private String extName;

    public TestDataLoaderImpl() {
        extName = RunnerConfig.getInstance().getTestCaseExtName();
        yaml = new Yaml(new Constructor(JSONObject.class));
    }

    @Override
    public T load(String testDataName, Class clazz) {
        T result;
        Set<String> executePaths = RunnerConfig.getInstance().getExecutePaths();
        if (executePaths.size() > 0) {
            return load(new File(testDataName), clazz);
        } else {
            StringBuffer classResourcetestDataPath = new StringBuffer();
            classResourcetestDataPath.append(testDataName).append(".");
            classResourcetestDataPath.append(extName);
            MyLog.debug("test case resources path: {}", classResourcetestDataPath.toString());
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(classResourcetestDataPath.toString());
            if (inputStream == null) {
                String exceptionMsg = String.format("in resources the testData %s is not exists.", classResourcetestDataPath);
                throw new DefinedException(exceptionMsg);
            }
            try {
                result = getObject(testDataName, clazz, inputStream);
                String validateResult = SchemaValidator.validateJsonObjectFormat(clazz, result);
                if (StrUtil.isEmpty(validateResult)) {
                    return result;
                } else {
                    if (clazz == TestCase.class) {
                        ApiModel apiModel = (ApiModel) getObject(testDataName, ApiModel.class, inputStream);
                        String validateInfo = SchemaValidator.validateJsonObjectFormat(ApiModel.class, apiModel);
                        if (StrUtil.isEmpty(validateInfo)) {
                            return (T) ObjectConverter.api2TestCase(apiModel);
                        } else {
                            throw new DefinedException(validateResult);
                        }
                    } else {
                        throw new DefinedException(validateResult);
                    }
                }
            } catch (DefinedException definedException) {
                throw definedException;
            } catch (Exception e) {
                String exceptionMsg = String.format("read file %s.%s occur exception: %s", testDataName, extName, e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
    }

    private T getObject(String testDataName, Class clazz, InputStream inputStream) {
        T result;
        if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                result = (T) mapper.readValue(inputStream, clazz);
            } catch (IOException e) {
                String exceptionMsg = String.format("read file %s.%s occur io exception: %s", testDataName, extName, e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        } else if (Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
            JSONObject jsonObject = yaml.load(inputStream);
            result = (T) jsonObject.toJavaObject(clazz);
        } else {
            String exceptionMsg = String.format("not support %s format,you can implement ITestDataLoader.java and try override load() method", extName);
            throw new DefinedException(exceptionMsg);
        }
        if (Objects.isNull(result)) {
            String exceptionMsg = String.format("文件%s内容不能为空", testDataName);
            throw new DefinedException(exceptionMsg);
        }
        return result;
    }

    @Override
    public T load(File fileName, Class clazz) {
        T testData;
        if (!fileName.exists()) {
            String exceptionMsg = String.format("file %s not found exception", fileName);
            throw new DefinedException(exceptionMsg);
        }
        String testDataName = fileName.getName();
        try {
            testData = getObject(testDataName, clazz, new FileInputStream(fileName));
            String validateResult = SchemaValidator.validateJsonObjectFormat(clazz, testData);
            if (StrUtil.isEmpty(validateResult)) {
                return testData;
            } else {
                if (clazz == TestCase.class) {
                    ApiModel apiModel = (ApiModel) getObject(testDataName, ApiModel.class, new FileInputStream(fileName));
                    String validateInfo = SchemaValidator.validateJsonObjectFormat(ApiModel.class, apiModel);
                    if (StrUtil.isEmpty(validateInfo)) {
                        return (T) ObjectConverter.api2TestCase(apiModel);
                    } else {
                        throw new DefinedException(validateResult);
                    }
                } else {
                    throw new DefinedException(validateResult);
                }
            }
        } catch (DefinedException definedException) {
            throw definedException;
        } catch (Exception e) {
            String exceptionMsg = String.format("read file %s.%s occur exception: %s", testDataName, extName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }
}
