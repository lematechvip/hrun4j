package io.lematech.httprunner4j.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ExpressHandler
 * @description TODO
 * @created 2021/1/26 5:20 下午
 * @publicWechat lematech
 */
public class ExpressHandler<T> {
    /**
     * 处理表达式语言
     * @param t
     * @return
     */
    public T handleExpress(T t, Map env){
        if (t instanceof Map){
            Map instance = (Map) t;
            Map<String,String> result = new HashMap<>();
            Iterator it = instance.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<String,Object> entry = (Map.Entry)it.next();
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                result.put(RegExpUtil.buildNewString(key,env),RegExpUtil.buildNewString(value,env));
            }
            return (T)result;
        }
        return t;
    }
}
