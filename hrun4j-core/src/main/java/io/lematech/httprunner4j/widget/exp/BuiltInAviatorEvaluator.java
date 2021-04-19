package io.lematech.httprunner4j.widget.exp;


import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import io.lematech.httprunner4j.HttpRunner4j;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.Env;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className BuiltInAviatorEvaluator
 * @description build in aviator evaluator
 * @created 2021/1/25 2:06 下午
 * @publicWechat lematech
 */
@Slf4j
public class BuiltInAviatorEvaluator {
    static {
        AviatorEvaluator.addFunction(new BuiltInFunctionEnv());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionAdd());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionSubtract());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionMultiply());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionDivide());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedHookFunction());
        AviatorEvaluator.addFunction(new HttpRunner4j.SignGenerateFunction());
        AviatorEvaluator.addFunction(new HttpRunner4j.ReuqestAndResponseHook());
    }

    public static Object execute(String expression, Map<String, Object> env) {
        Expression compiledExp = AviatorEvaluator.compile(expression, false);
        return compiledExp.execute(env);
    }

    /**
     * built-in $ENV
     */
    public static class BuiltInFunctionEnv extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            Object envValue = arg1.getValue(Env.getEnvMap());
            if (Objects.isNull(envValue)) {
                String exceptionMsg = String.format("env not found key : %s", arg1);
                throw new DefinedException(exceptionMsg);
            }
            return new AviatorString(String.valueOf(envValue));
        }
        @Override
        public String getName() {
            return "ENV";
        }
    }

}
