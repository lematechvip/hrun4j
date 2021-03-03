package io.lematech.httprunner4j.test.utils;

import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.test.common.Constant;

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
     * validate java variable name valid
     * @param name
     * @return
     */
    public static boolean isValidJavaIdentifier(String name){
        if(StrUtil.isEmpty(name)){
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
