package vip.lematech.hrun4j.base;

import cn.hutool.core.io.FileUtil;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.core.provider.NGDataProvider;
import vip.lematech.hrun4j.helper.LogHelper;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Test base classes for pre -, post -, and data loading
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 *
 */

public class TestBase {
    private String testCaseName;
    @BeforeSuite
    public void beforeSuite(){
        LogHelper.info("[========================================]@beforeSuite()");
    }
    @BeforeMethod
    public void setUp() {
        LogHelper.info("[====================" + this.testCaseName + "====================]@START");
    }
    @AfterMethod
    public void tearDown() {
        LogHelper.info("[====================" + this.testCaseName + "====================]@END");
    }
    @AfterSuite
    public void afterSuite(){
        LogHelper.info("[========================================]@afterSuite()");
    }
    @DataProvider
    public Object[][] dataProvider(Method method) {
        Object[][] objects;
        this.testCaseName = method.getName();
        String packageName = FileUtil.mainName(method.getDeclaringClass().getName());
        try {
            objects = new NGDataProvider().dataProvider(packageName, testCaseName);
        } catch (DefinedException e) {
            throw e;
        } catch (Exception e) {
            String exceptionMsg = String.format("Abnormal testng data loading occurs, and the reason for the exception is as follows: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return objects;
    }
}
