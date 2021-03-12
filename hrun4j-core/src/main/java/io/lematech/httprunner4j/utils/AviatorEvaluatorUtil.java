package io.lematech.httprunner4j.utils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import io.lematech.httprunner4j.HttpRunner4j;

import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className AviatorEvaluatorUtil
 * @description TODO
 * @created 2021/1/25 2:06 下午
 * @publicWechat lematech
 */
public class AviatorEvaluatorUtil {
    static {
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionAdd());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionSubtract());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionMultiply());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionDivide());
    }
    public static Object execute(String expression, Map<String, Object> env){
        Expression compiledExp = AviatorEvaluator.compile(expression, false);
        return compiledExp.execute(env);
    }
}
