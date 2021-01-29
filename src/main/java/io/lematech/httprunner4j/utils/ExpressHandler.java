package io.lematech.httprunner4j.utils;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
@Data
@Slf4j
public class ExpressHandler<T> {

    private Map<String,Object> currentEnv = new HashMap<>();
    private Map<String,Object> configVars = new HashMap<>();
    private Map<String,Object> testStepVars = new HashMap<>();
    /**
     * 处理表达式语言
     * @param t
     * @return
     */
    public T handleExpress(T t){
        if (t instanceof Map){
            Map instance = (Map) t;
            Map<String,Object> result = new HashMap<>();
            Iterator it = instance.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<String,Object> entry = (Map.Entry)it.next();
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                Class valueClass = entry.getValue().getClass();
                if(valueClass == Integer.class){
                    Integer valueObj = Integer.parseInt(RegExpUtil.buildNewString(value,this.currentEnv));
                    result.put(RegExpUtil.buildNewString(key,this.currentEnv),valueObj);
                }else if(valueClass == String.class){
                    result.put(RegExpUtil.buildNewString(key,this.currentEnv),RegExpUtil.buildNewString(value,this.currentEnv));
                }
            }
            return (T)result;
        }else if(t instanceof String){
            String str =(String) t;
            return (T)RegExpUtil.buildNewString(str,this.currentEnv);
        }
        return t;
    }

    private void initEnvironment(Map<String,Object> configVars,Map<String,Object> testStepVars){
        this.configVars = (Map<String, Object>) this.handleExpress((T) configVars);
        this.testStepVars = (Map<String, Object>) this.handleExpress((T) testStepVars);
    }

    public void buildCurrentEnv(Map<String,Object> testContext,Map<String,Object> configVars,Map<String,Object> testStepVars){
        this.currentEnv = testContext;
        initEnvironment(configVars,testStepVars);
        Map<String,Object> resEnv = new HashMap<>();
        resEnv.putAll(this.configVars);
        resEnv.putAll(this.testStepVars);
        resEnv.putAll(testContext);
        this.currentEnv = resEnv;
    }

    public Object buildNewObj(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        int fieldLength = fields.length;
        for(int index = 0;index<fieldLength;index++){
            String attributeName  = fields[index].getName();
            Class attributeClass = fields[index].getType();
            fields[index].setAccessible(true);
            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
            try{
                Object fieldValue = getFieldValueByName(fields[index].getName(), object);
                if(fieldValue == null){
                   continue;
                }
                if(attributeClass == String.class && StrUtil.isEmpty(String.valueOf(fieldValue))){
                    continue;
                }
                log.info("类型：{}",attributeClass);
                if(attributeClass== BigDecimal.class){
                    Method setMethod=object.getClass().getMethod("set"+methodName,BigDecimal.class);
                    setMethod.invoke(object,new BigDecimal(0));
                }else if(attributeClass==Long.class){
                    Method setMethod=object.getClass().getMethod("set"+methodName,Long.class);
                    setMethod.invoke(object,0L);
                }else if(attributeClass==String.class){
                    Method setMethod=object.getClass().getMethod("set"+methodName,String.class);
                    setMethod.invoke(object,this.handleExpress((T) fieldValue));
                }else if(attributeClass==Map.class){
                    Method setMethod=object.getClass().getMethod("set"+methodName,Map.class);
                    setMethod.invoke(object,this.handleExpress((T) fieldValue));
                }
            }catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
