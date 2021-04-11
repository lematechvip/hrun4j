package io.lematech.httprunner4j.utils;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JavaIdentifierUtil
 * @description TODO
 * @created 2021/1/28 4:59 下午
 * @publicWechat lematech
 */

public class JavaIdentifierUtil {

    /**
     * identifierName transfer to valid java  identifier
     * char value replace '_' if it not validate
     *
     * @param identifierName
     * @param type           0 : 非包名，1：包名
     * @return
     */
    public static String toValidJavaIdentifier(String identifierName, Integer type) {
        if (StrUtil.isEmpty(identifierName)) {
            String exceptionMsg = String.format(" name {} is invalid,not apply java identifier,please modify it", identifierName);
            throw new DefinedException(exceptionMsg);
        }
        StringBuffer validName = new StringBuffer();
        char[] nameChars = identifierName.toCharArray();
        for (int index = 0; index < nameChars.length; index++) {
            char tmpChar = nameChars[index];
            if (tmpChar == '.' && type == 1) {
                validName.append(".");
                continue;
            }
            if (tmpChar == ' ') {
                continue;
            }
            if (index == 0) {
                if (Character.isJavaIdentifierStart(tmpChar)) {
                    validName.append(tmpChar);
                } else {
                    validName.append("_");
                }
            } else {
                if (Character.isJavaIdentifierPart(tmpChar)) {
                    validName.append(tmpChar);
                } else {
                    validName.append("_");
                }
            }
        }
        return validName.toString();
    }

    /**
     * validate java variable name valid
     *
     * @param name
     * @return
     */
    public static boolean isValidJavaIdentifier(String name) {
        if (StrUtil.isEmpty(name)) {
            return false;
        }
        char []nameChars = name.toCharArray();
        for(int index = 0;index<nameChars.length;index++){
            char tmpChar = nameChars[index];
            if(index == 0){
                if(!Character.isJavaIdentifierStart(tmpChar)){
                    return false;
                }
            }else {
                if(!Character.isJavaIdentifierPart(tmpChar)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * validate package name or validate package and class
     * @param fullName
     * @return
     */
    public static boolean isValidJavaFullClassName(String fullName){
        if(fullName.equals("")){
            return false;
        }
        boolean flag = true;
        try{
            if(!fullName.endsWith(Constant.DOT_PATH)){
                int index = fullName.indexOf(Constant.DOT_PATH);
                if(index!=-1){
                    String[] str = fullName.split("\\.");
                    for(String name : str){
                        if(name.equals("")){
                            flag = false;
                            break;
                        }else if(!isValidJavaIdentifier(name)){
                            flag = false;
                            break;
                        }
                    }
                }else if(!isValidJavaIdentifier(fullName)){
                    flag = false;
                }
            }else {
                flag = false;
            }
        }catch(Exception ex){
            flag = false;
            ex.printStackTrace();
        }
        return flag;
    }


}
