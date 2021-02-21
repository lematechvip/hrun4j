package io.lematech.httprunner4j.base;

import io.lematech.httprunner4j.NGDataProvider;
import io.lematech.httprunner4j.common.DefinedException;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;


@Slf4j
public class NGTestBase extends TestBase {

    @DataProvider
    public Object[][] dataProvider(Method method) {
        Object[][] objects ;
        try{
            String pkgName = fromClassExtractPkg(method.getDeclaringClass().getName());
            objects = NGDataProvider.dataProvider(pkgName,method.getName());
        }catch (Exception e){
            String exceptionMsg = String.format("testcase %s ,data provider occur exception: %s",method.getName(),e.getMessage());
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
