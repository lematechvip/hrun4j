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
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestDataLoaderImpl
 * @description Data loading implementation class, support file loading
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */

public class TestDataLoaderImpl<T> implements ITestDataLoader {
    private Yaml yaml;
    private String extName;
    private ObjectMapper mapper;

    public TestDataLoaderImpl(String extName) {
        this.extName = extName;
        this.yaml = new Yaml(new Constructor(JSONObject.class));
        mapper = new ObjectMapper();
    }

    /**
     * File serialization to object
     *
     * @param clazz
     * @param file
     * @return
     */
    private T fileSerialization2Object(File file, Class clazz) {
        T result;
        String testDataName = file.getName();
        try {
            if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)) {
                result = (T) mapper.readValue(new FileInputStream(file), clazz);
            } else if (Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
                JSONObject jsonObject = yaml.load(new FileInputStream(file));
                result = (T) jsonObject.toJavaObject(clazz);
            } else {
                String exceptionMsg = String.format("The current format %s is not currently supported,you can implement ITestDataLoader interface and try to override load() method", extName);
                throw new DefinedException(exceptionMsg);
            }
            if (Objects.isNull(result)) {
                String exceptionMsg = String.format("The serialized file %s cannot be empty", testDataName);
                throw new DefinedException(exceptionMsg);
            }
        } catch (IOException e) {
            String exceptionMsg = String.format("Error in file %s.%s serialization,Exception Information: %s", testDataName, extName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return result;
    }

    /**
     * File load
     * @param fileName
     * @param clazz
     * @return
     */
    @Override
    public T load(File fileName, Class clazz) {
        T testData;
        FilesUtil.fileValidate(fileName);
        String testDataName = fileName.getName();
        try {
            testData = fileSerialization2Object(fileName, clazz);
            String validateResult = SchemaValidator.validateJsonObjectFormat(clazz, testData);
            if (StrUtil.isEmpty(validateResult)) {
                return testData;
            } else {
                if (clazz == TestCase.class) {
                    ApiModel apiModel = (ApiModel) fileSerialization2Object(fileName, ApiModel.class);
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
            String exceptionMsg = String.format("An exception occurred in the loading %s file. Exception information:%s", testDataName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }
}
