package io.lematech.httprunner4j.testng;

import io.lematech.httprunner4j.NGDataProvider;
import io.lematech.httprunner4j.TestCaseExecutorEngine;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Slf4j
public class BuildTestNGAndExecMy extends TestBase {

    @Test(dataProvider = "dataProvider")
    public void buildTestNGAndExe(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * method.getDeclaringClass().getPackage().getName()
     * avoid dyn load class ,getpakcage nullpointerexception
     * @param className
     * @return
     */
    public String fromClassExtractPkg(String className) {
        if (className == null) {
            return null;
        } else {
            int index = className.lastIndexOf(".");
            if (index == -1) {
                return "";
            } else {
                String ext = className.substring(0,index);
                return ext;
            }
        }
    }
    @DataProvider
    public Object[][] dataProvider(Method method) {

        Object[][] objects ;
        try{
            String pkgName = fromClassExtractPkg(method.getDeclaringClass().getName());
            log.info("pkgName:{}",pkgName);
            log.info("pkgName:{}",method.getDeclaringClass().getPackage().getName());
            objects = NGDataProvider.dataProviderExistInstance(new TestCase());
        }catch (Exception e){
            String exceptionMsg = String.format("testcase %s ,data provider occur exception: %s",method.getName(),e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return objects;
    }

}
