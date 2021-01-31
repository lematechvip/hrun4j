package io.lematech.httprunner4j.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className RegExp
 * @description TODO
 * @created 2021/1/26 2:09 下午
 * @publicWechat lematech
 */
@Slf4j
public class RegExp {

    private static List<String> getExpressList(String str, List<Integer> indexs){
        List<String> exps = new ArrayList<>();
        for(int index = 0;index<indexs.size();index++){
            int startIndex = indexs.get(index);
            int endIndex = indexs.get(++index);
            String subStr = str.substring(startIndex,endIndex);
            exps.add(subStr);
        }
        return exps;
    }
    public static String buildNewString(String paramStr, Map env){
        log.info("处理前参数值：{}",paramStr);
        char [] myChars = paramStr.toCharArray();
        List<Object> handlerResult = new ArrayList<>();;
        List<Integer> indexs = RegExp.getExpressIndexs(myChars);
        if(RegExp.validateIndexs(indexs)){
            List<String> exps = RegExp.getExpressList(paramStr,indexs);
            for(String exp : exps){
                handlerResult.add(AviatorEvaluatorUtil.execute(exp,env));
            }
            StringBuilder stringBuilder = new StringBuilder();
            Integer size = myChars.length - 1;
            boolean startFlag = true;
            for (int index = 0,resultIndex = 0;index<=size;index++){
                if('`' == myChars[index]&&'\\' != myChars[index == 0 ? index:index-1]){
                    if(startFlag){
                        startFlag = false;
                    }else{
                        stringBuilder.append(handlerResult.get(resultIndex++));
                        startFlag = true;
                    }
                }else{
                    if(startFlag){
                        stringBuilder.append(myChars[index]);
                    }
                }
            }
            log.info("处理前参数值：{},处理后参数值：{}",paramStr,stringBuilder.toString());
            return stringBuilder.toString();
        }else{
            log.info("处理前参数值：{},处理后参数值：{}",paramStr,paramStr);
           return paramStr;
        }
    }
    private static List<Integer> getExpressIndexs(char[] myChars){
        List<Integer> indexs = new ArrayList<>();
        Integer size = myChars.length - 1;
        boolean startFlag = true;
        for (int index = 0;index<=size;index++){
            if('`' == myChars[index]&&'\\' != myChars[index == 0 ? index:index-1]){
                if(startFlag){
                    indexs.add(index+1);
                    startFlag = false;
                }else{
                    indexs.add(index);
                    startFlag = true;
                }
            }
        }
        return indexs;
    }
    private static  Boolean validateIndexs(List<Integer> indexs){
        int size = indexs.size();
        if(indexs == null || size == 0){
            return false;
        }
        if(size % 2 == 0){
            return true;
        }
        return false;
    }
}
