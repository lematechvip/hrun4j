package vip.lematech.httprunner4j.functions;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import vip.lematech.httprunner4j.helper.LogHelper;


import java.util.Map;

/**
 * build in fuction
 *
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public class BuildInFunctions {

    public static final String TOKEN_KEY = "httprunner4j";
    public static class ReuqestAndResponseHook extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg) {
            LogHelper.info("当前请求详细信息：{}", env.get("$REQUEST"));
            LogHelper.info("当前响应详细信息：{}", env.get("$RESPONSE"));
            return new AviatorString(String.valueOf(arg));
        }
        @Override
        public String getName() {
            return "RRHook";
        }
    }

    public static class DefinedHookFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg) {
            LogHelper.info("当前方法名：{},入参信息：{},环境变量：{}", this.getName(), arg.getAviatorType(), env);
            return new AviatorString(String.valueOf(arg));
        }
        @Override
        public String getName() {
            return "hook";
        }
    }

    public static class DefinedFunctionAdd extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
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
    public static class SignGenerateFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
            String sign;
            StringBuffer content = new StringBuffer();
            content.append(arg1.getValue(env)).append(arg2.getValue(env)).append(arg3.getValue(env));
            String crypContent = content.toString();
            sign = SecureUtil.hmac(HmacAlgorithm.HmacSHA1, TOKEN_KEY).digestHex(crypContent);
            LogHelper.info("加密秘钥：{},加密内容：{},生成的签名：{}", TOKEN_KEY, crypContent, sign);
            return new AviatorString(sign);
        }
        @Override
        public String getName() {
            return "signGenerate";
        }
    }
}
