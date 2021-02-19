package io.lematech.httprunner4j;

import cn.hutool.core.util.StrUtil;

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

}
