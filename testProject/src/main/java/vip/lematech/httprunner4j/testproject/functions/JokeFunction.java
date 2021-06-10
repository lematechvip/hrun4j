package vip.lematech.httprunner4j.testproject.functions;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import vip.lematech.httprunner4j.widget.log.MyLog;
import java.util.Map;

/**
* @author lematech@foxmail.com
* @version 1.0.0
*/
public class JokeFunction {
    /**
    * Custom function, set the function name will be implemented in the call supplement function
    */
    public static class JokeSetupHook extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject page, AviatorObject type, AviatorObject count) {
            MyLog.info("正在执行：{}方法，方法参数：page={},count={},type={}",this.getName(), page,count,type);
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
    public static class JokeTearDownHook extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject type) {
            MyLog.info("正在执行：{}方法，方法参数：{}",this.getName(),type.toString());
            MyLog.info("当前请求参数详细信息：{}", env.get("$REQUEST"));
            MyLog.info("当前响应参数详细信息：{}", env.get("$RESPONSE"));
            return new AviatorString("defineResult");
        }
        @Override
        public String getName() {
            return "tdFunction";
        }
    }
}
