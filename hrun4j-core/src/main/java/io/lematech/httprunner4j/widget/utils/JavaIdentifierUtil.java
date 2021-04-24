package io.lematech.httprunner4j.widget.utils;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JavaIdentifierUtil
 * @description java identifier validation
 * @created 2021/1/28 4:59 下午
 * @publicWechat lematech
 */

public class JavaIdentifierUtil {

    /**
     * validate method、classname valid
     * tips：Extension support "-", runtime auto transfer "_"
     *
     * @param className
     * @return
     */
    public static boolean isValidJavaIdentifier(String className) {
        if (StrUtil.isEmpty(className) || !Character.isJavaIdentifierStart(className.charAt(0))) {
            return false;
        }
        String name = className.substring(1);
        for (int i = 0; i < name.length(); i++) {
            char tmpChar = name.charAt(i);
            if (!Character.isJavaIdentifierPart(tmpChar)) {
                return false;
            }
        }
        return true;
    }

    /**
     *  validate package name or validate package and class
     * @param fullName
     * @return
     */
    public static boolean isValidJavaFullClassName(String fullName) {
        if (StrUtil.isEmpty(fullName)) {
            return false;
        }
        boolean flag = true;
        if (!fullName.endsWith(Constant.DOT_PATH)) {
            int index = fullName.indexOf(Constant.DOT_PATH);
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
        return flag;
    }


    /**
     * Verify that the package name is valid
     *
     * @param identifierName
     * @return
     */
    public static String validateIdentifierName(String identifierName) {
        String validateInfo = "";
        if (StrUtil.isEmpty(identifierName)) {
            return " The package  name cannot be empty";
        }
        if (!identifierName.endsWith(Constant.DOT_PATH)) {
            int index = identifierName.indexOf(Constant.DOT_PATH);
            if (index != -1) {
                String[] str = identifierName.split("\\.");
                for (String name : str) {
                    if (StrUtil.isEmpty(name)) {
                        return String.format(" The package/directory name [%s] ,sub package name  cannot be empty", identifierName);
                    } else if (!isValidJavaIdentifier(name)) {
                        return String.format(" The package/directory name [%s] ,sub package name [%s] is not valid", identifierName, name);
                    }
                }
            } else if (!isValidJavaIdentifier(identifierName)) {
                return String.format(" The package/directory name [%s] is not valid", identifierName);
            }
        } else {
            return String.format(" The package/directory name [%s] can not ends with '.'", identifierName);
        }
        return validateInfo;
    }

}
