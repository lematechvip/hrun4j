package io.lematech.httprunner4j.junit.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.testng.annotations.Test;

import java.io.File;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JavaUtil
 * @description TODO
 * @created 2021/4/22 4:58 下午
 * @publicWechat lematech
 */
public class JavaUtil {
    @Test
    public void javaUtilTest() {

        // System.out.println( FileUtil.normalize("./test"));
        /*String fullName = "com.sdf.cccc.Test";
        JavaUtil javaTest = new JavaUtil();
        if (javaTest.isValidJavaFullClassName(fullName)) {
            System.out.println(fullName + "  符合JAVA命名规范");
        } else {
            System.out.println(fullName + "  不符合JAVA命名规范");
        }*/

    }

    /**
     * validate method、classname valid
     *
     * @param className
     * @return
     */
    private boolean isValidJavaIdentifier(String className) {
        if (StrUtil.isEmpty(className) || !Character.isJavaIdentifierStart(className.charAt(0))) {
            return false;
        }
        String name = className.substring(1);
        for (int i = 0; i < name.length(); i++) {
            char tmpChar = name.charAt(i);
            if (!Character.isJavaIdentifierPart(tmpChar) || '-' == tmpChar) {
                return false;
            }
        }
        return true;
    }

    /**
     * validate package name or validate package and class
     *
     * @param fullName
     * @return
     */
    public boolean isValidJavaFullClassName(String fullName) {
        if (StrUtil.isEmpty(fullName)) {
            return false;
        }
        boolean flag = true;
        try {
            if (!fullName.endsWith(".")) {
                int index = fullName.indexOf(".");
                if (index != -1) {
                    String[] str = fullName.split("\\.");
                    for (String name : str) {
                        if (StrUtil.isEmpty(name)) {
                            flag = false;
                            break;
                        } else if (!isValidJavaIdentifier(name)) {
                            flag = false;
                            break;
                        }
                    }
                } else if (!isValidJavaIdentifier(fullName)) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        } catch (Exception ex) {
            flag = false;
        }
        return flag;
    }

}
