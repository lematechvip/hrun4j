package io.lematech.httprunner4j.base;

import io.lematech.httprunner4j.NGDataProvider;
import io.lematech.httprunner4j.common.DefinedException;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.lang.reflect.Method;


@Slf4j
public class TestBase {
    private String testCaseName;
    @BeforeSuite
    public void beforeSuite(){
        log.info("[========================================]@beforeSuite()");
    }
    @BeforeMethod
    public void setUp() {
        log.info("[===================="+this.testCaseName+"====================]@START");
    }
    @AfterMethod
    public void tearDown() {
        log.info("[===================="+this.testCaseName+"====================]@END");
    }
    @AfterSuite
    public void afterSuite(){
        log.info("[========================================]@afterSuite()");
    }
    @DataProvider
    public Object[][] dataProvider(Method method) {
        Object[][] objects ;
        try{
            this.testCaseName = method.getName();
            String pkgName = fromClassExtractPkg(method.getDeclaringClass().getName());
            objects = NGDataProvider.dataProvider(pkgName,testCaseName);
        }catch (Exception e){
            e.printStackTrace();
            String exceptionMsg = String.format("testcase %s ,data provider occur exception: %s",testCaseName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return objects;
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
}