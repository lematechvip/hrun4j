package io.lematech.httprunner4j.handler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Handler
 * @description TODO
 * @created 2021/1/20 6:27 下午
 * @publicWechat lematech
 */
@Slf4j
public class Handler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private String testCaseName;
    public Handler() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private TestCase load() {
        Yaml yaml = new Yaml(new Constructor(JSONObject.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("testcases/" + this.testCaseName);
        return yaml.load(inputStream);
    }

    public TestCase load(String fileName) {
        this.testCaseName = fileName;
        Yaml yaml = new Yaml(new Constructor(JSONObject.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("testcases/" + this.testCaseName+".yml");
        JSONObject testCaseMetas = yaml.load(inputStream);
        TestCase testCase = testCaseMetas.toJavaObject(TestCase.class);
        return testCase;
    }
}