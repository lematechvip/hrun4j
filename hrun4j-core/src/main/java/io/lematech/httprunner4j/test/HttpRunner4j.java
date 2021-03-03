package io.lematech.httprunner4j.test;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className HttpRunner4j
 * @description TODO
 * @created 2021/1/25 2:49 下午
 * @publicWechat lematech
 */
public class HttpRunner4j {
     public static class DefinedFunctionAdd extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            // java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Number
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorBigInt(Math.addExact(left.intValue(), right.intValue()));
        }
        @Override
        public String getName() {
            return "add";
        }
    }
    public static class DefinedFunctionSubtract extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorBigInt(Math.subtractExact(left.intValue(), right.intValue()));
        }
        @Override
        public String getName() {
            return "subtract";
        }
    }
    public static class DefinedFunctionMultiply extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorBigInt(Math.multiplyExact(left.intValue(), right.intValue()));
        }
        @Override
        public String getName() {
            return "multiply";
        }
    }
    public static class DefinedFunctionDivide extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorBigInt(Math.multiplyExact(left.intValue(), right.intValue()));
        }
        @Override
        public String getName() {
            return "divide";
        }
    }

}
