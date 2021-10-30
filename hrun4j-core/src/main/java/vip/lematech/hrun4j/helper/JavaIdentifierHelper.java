package vip.lematech.hrun4j.helper;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;

/**
 * java identifier validation
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class JavaIdentifierHelper {

    /**
     * validate method、classname valid
     * tips：Extension support "-", runtime auto transfer "_"
     *
     * @param className The class name
     * @return true or false
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
     * validate package name or validate package and class
     *
     * @param fullName full name
     * @return true or false
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
     * @param identifierName identifier name
     * @return If it is null, the validation passes, anyway, the reason for the validation failure
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



    /**
     * format that the file path meets the requirements
     *
     * @param filePath The file path
     * @return format that the file path meets the requirements
     */
    public static String formatFilePath(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            String exceptionMsg = String.format(" The file path cannot be empty");
            throw new DefinedException(exceptionMsg);
        }
        filePath = FileUtil.normalize(filePath);
        StringBuffer validName = new StringBuffer();
        char[] nameChars = filePath.toCharArray();
        for (int index = 0; index < nameChars.length; index++) {
            char tmpChar = nameChars[index];
            if (!Character.isJavaIdentifierPart(tmpChar)) {
                validName.append(Constant.UNDERLINE);
            } else {
                validName.append(tmpChar);
            }
        }
        return validName.toString();
    }
}
