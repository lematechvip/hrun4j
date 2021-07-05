package vip.lematech.hrun4j.functions;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import vip.lematech.hrun4j.helper.LogHelper;

import java.util.Map;

/**
* @author lematech@foxmail.com
* @version 1.0.1
*/

public class MyFunction {

    /**
    * Custom function, set the function name will be implemented in the call supplement function
    */
    public static class SetupHookFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject page, AviatorObject type, AviatorObject count) {
            LogHelper.info("正在执行：{}方法，方法参数：page={},count={},type={}",this.getName(), page,count,type);
            String typeValue = FunctionUtils.getStringValue(type, env);
            Number pageValue = FunctionUtils.getNumberValue(page, env);
            Number countValue = FunctionUtils.getNumberValue(count, env);
            String spiceString = String.format("page=%s&count=%s&page=type",pageValue,countValue,typeValue);
            return new AviatorString(String.valueOf(spiceString));
        }
        @Override
        public String getName() {
            return "shFunction";
        }
    }

    /**
    * Custom function, set the function name will be implemented in the call supplement function
    */
    public static class TearDownHookFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject type) {
            LogHelper.info("正在执行：{}方法，方法参数：{}",this.getName(),type.toString());
            LogHelper.info("当前请求参数详细信息：{}", env.get("$REQUEST"));
            LogHelper.info("当前响应参数详细信息：{}", env.get("$RESPONSE"));
            return new AviatorString("defineResult");
        }
        @Override
        public String getName() {
            return "tdFunction";
        }
    }


    public static final String TOKEN_KEY = "hrun4j";
    public static class RequestAndResponseHook extends AbstractFunction {
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
            StringBuffer content = new StringBuffer();
            content.append(arg1.getValue(env)).append(arg2.getValue(env)).append(arg3.getValue(env));
            String crypContent = content.toString();
            String sign = SecureUtil.hmac(HmacAlgorithm.HmacSHA1, TOKEN_KEY).digestHex(crypContent);
            LogHelper.info("加密秘钥：{},加密内容：{},生成的签名：{}", TOKEN_KEY, crypContent, sign);
            return new AviatorString(sign);
        }
        @Override
        public String getName() {
            return "signGenerate";
        }
    }

}
