package io.lematech.httprunner4j;

import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
public class TestCaseYmlLoaderImpl implements ITestCaseLoader {
    private Yaml yaml ;
    public  TestCaseYmlLoaderImpl(){
        yaml = new Yaml(new Constructor(JSONObject.class));
    }

    @Override
    public TestCase load(String testCaseName) {
        StringBuffer classResourceTestCasePath = new StringBuffer();
        classResourceTestCasePath.append(testCaseName)
                .append(".");
        classResourceTestCasePath
                .append(Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME);
        InputStream inputStream =  this.getClass().getResourceAsStream(classResourceTestCasePath.toString());
        if(inputStream == null){
            String exceptionMsg = String.format("in resources the testcase %s is not exists.",classResourceTestCasePath);
            throw new DefinedException(exceptionMsg);
        }
        JSONObject testCaseMetas = yaml.load(inputStream);
        return testCaseMetas.toJavaObject(TestCase.class);
    }

    @Override
    public TestCase load(File fileName) {
        TestCase testCase ;
        if(!fileName.exists()){
            fileName = new File(Constant.TEST_CASE_FILE_PATH+File.separator+fileName);
        }
        try {
            InputStream inputStream = new FileInputStream(fileName);
            JSONObject testCaseMetas = yaml.load(inputStream);
            testCase = testCaseMetas.toJavaObject(TestCase.class);
        } catch (FileNotFoundException e) {
            String exceptionMsg = String.format("file %s not found exception",fileName);
            throw new DefinedException(exceptionMsg);
        } catch (Exception e){
            String exceptionMsg = String.format("test case %s file wrong format. exception:%s",fileName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return testCase;
    }
}
