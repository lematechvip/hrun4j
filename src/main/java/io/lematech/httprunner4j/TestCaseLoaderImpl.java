package io.lematech.httprunner4j;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class TestCaseLoaderImpl implements ITestCaseLoader {
    private Yaml yaml ;
    public  TestCaseLoaderImpl(){
        yaml = new Yaml(new Constructor(JSONObject.class));
    }

    @Override
    public TestCase load(String testCaseName,String extName) {
        List<String> executePaths = RunnerConfig.getInstance().getExecutePaths();
        if(executePaths.size()>0){
            return load(new File(testCaseName));
        }else{
            StringBuffer classResourceTestCasePath = new StringBuffer();
            classResourceTestCasePath.append(testCaseName).append(".");
            classResourceTestCasePath.append(extName);
            log.debug("test case resources path: {}",classResourceTestCasePath.toString());
            InputStream inputStream =  this.getClass().getClassLoader().getResourceAsStream(classResourceTestCasePath.toString());
            if(inputStream == null){
                String exceptionMsg = String.format("in resources the testcase %s is not exists.",classResourceTestCasePath);
                throw new DefinedException(exceptionMsg);
            }
            try {
                if(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)){
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(inputStream, TestCase.class);
                }else if(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)){
                    JSONObject  jsonObject = yaml.load(inputStream);
                    return jsonObject.toJavaObject(TestCase.class);
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
    public TestCase load(File fileName) {
        TestCase testCase ;
        if(!fileName.exists()){
            String exceptionMsg = String.format("file %s not found exception",fileName);
            throw new DefinedException(exceptionMsg);
        }
        String extName = RunnerConfig.getInstance().getTestCaseExtName();
        try {
            if(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)){
                ObjectMapper mapper = new ObjectMapper();
                testCase = mapper.readValue(fileName, TestCase.class);
            }else if(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)){
                JSONObject  jsonObject = yaml.load(new FileInputStream(fileName));
                return jsonObject.toJavaObject(TestCase.class);
            }else {
                String exceptionMsg = String.format("not support %s format,you can implement ITestCaseLoader.java and try override load() method",extName);
                throw new DefinedException(exceptionMsg);
            }
        }catch (Exception e) {
            e.printStackTrace();
            String exceptionMsg = String.format("read file %s.%s occur exception: %s",fileName,extName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return testCase;
    }
}
