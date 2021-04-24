package io.lematech.httprunner4j.base;

import cn.hutool.core.io.FileUtil;
import com.googlecode.aviator.AviatorEvaluator;
import io.lematech.httprunner4j.widget.exp.HttpRunner4j;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.core.provider.NGDataProvider;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.exp.BuiltInAviatorEvaluator;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestBase
 * @description TODO
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */

public class TestBase {
    private String testCaseName;
    @BeforeSuite
    public void beforeSuite(){
        MyLog.info("[========================================]@beforeSuite()");
    }
    @BeforeMethod
    public void setUp() {
        MyLog.info("[====================" + this.testCaseName + "====================]@START");
    }
    @AfterMethod
    public void tearDown() {
        MyLog.info("[====================" + this.testCaseName + "====================]@END");
    }
    @AfterSuite
    public void afterSuite(){
        MyLog.info("[========================================]@afterSuite()");
    }
    @DataProvider
    public Object[][] dataProvider(Method method) {
        Object[][] objects;
        this.testCaseName = method.getName();
        String packageName = FileUtil.mainName(method.getDeclaringClass().getName());
        objects = new NGDataProvider().dataProvider(packageName, testCaseName);
        return objects;
    }
}
