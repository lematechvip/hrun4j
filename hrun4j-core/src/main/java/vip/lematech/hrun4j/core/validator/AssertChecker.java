package vip.lematech.hrun4j.core.validator;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang.enums.EnumUtils;
import vip.lematech.hrun4j.config.i18n.I18NFactory;
import vip.lematech.hrun4j.helper.JsonHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.entity.http.ResponseEntity;
import vip.lematech.hrun4j.entity.testcase.Comparator;
import vip.lematech.hrun4j.core.processor.DataExtractor;
import vip.lematech.hrun4j.core.processor.ExpProcessor;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Created 2021/1/22 4:07 下午
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 * assert checker
 */
public class AssertChecker {

    /**
     * Data extractor
     */
    private DataExtractor dataExtractor;

    public AssertChecker(ExpProcessor expProcessor) {
        dataExtractor = new DataExtractor(expProcessor);
    }

    private static Map<String, String> alisaMap = new HashMap<>();
    /**
     * Built-in method alias manager
     */
    private static Map<String, String> builtInAlisaMap = new HashMap<>();

    /**
     * Dynamically build matcher object
     *
     * @param comparatorName
     * @param params
     * @param expect
     * @return
     */
    private static Matcher buildMatcherObj(String comparatorName, List<String> params, Object expect) {
        Object obj;
        try {
            Class<?> clzValue = Class.forName("org.hamcrest.Matchers");
            String methodName = alisaMap.containsKey(comparatorName) ? alisaMap.get(comparatorName) : comparatorName;
            Method method = null;
            if (params.size() == 0) {
                method = clzValue.getMethod(methodName);
            } else if (params.size() == 1) {
                method = clzValue.getMethod(methodName, Class.forName(params.get(0)));
            } else if (params.size() == 2) {
                method = clzValue.getMethod(methodName, Class.forName(params.get(0)), Class.forName(params.get(1)));
            }
            obj = method.invoke(null, expect);
        } catch (ClassNotFoundException e) {
            String exceptionMsg = String.format("Class not found exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (NoSuchMethodException e) {
            String exceptionMsg = String.format("No such method exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (IllegalAccessException e) {
            String exceptionMsg = String.format("Illegal access exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (InvocationTargetException e) {
            String exceptionMsg = String.format("Invocation target exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (Exception e) {
            String exceptionMsg = String.format("Dynamically build matcher object exceptions, exception information:: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return (Matcher)obj;
    }

    private static Map<String,String> extendedJsonComparator = new HashMap();
    /**
     *
     * @param objectMap Asserts the properties contained by the object
     * @param responseEntity Response structure
     * @param env Current environment variable
     */
    public void assertObject(Map<String, Object> objectMap, ResponseEntity responseEntity, Map<String, Object> env) {
        Map<String, List> methodAlisaMap = comparatorAlisaMap();
        Comparator comparator = buildComparator(objectMap);
        Object exp = comparator.getCheck();
        String comparatorName = comparator.getComparator();
        if (StrUtil.isEmpty(comparatorName)) {
            throw new DefinedException("The validation method name cannot be empty");
        }

        Object actual = dataExtractor.handleExpDataExtractor(exp, responseEntity);
        Object expect = comparator.getExpect();
        if (!methodAlisaMap.containsKey(comparatorName)) {
            if(extendedJsonComparator.containsKey(comparatorName)){
                String realComparator = extendedJsonComparator.get(comparatorName);
                JsonHelper.BaseResult baseResult = JsonHelper.jsonValidateFactory(realComparator,(JSONObject)JSONObject.toJSON(actual),(JSONObject)JSONObject.toJSON(expect),null);
                if(baseResult.getWrongNumber() != 0){
                    String msg = String.format("Json data or schema verification fails, detail reasons：");
                    ArrayList<JsonHelper.ResultDetail> resultDetails = baseResult.getResultDetail();
                    for(JsonHelper.ResultDetail resultDetail : resultDetails){
                        LogHelper.error("On the $.v node: :{},actual value:{},expect value:{}",resultDetail.getPrefix(),resultDetail.getActual(),resultDetail.getExpect(),resultDetail.getMsg());
                    }
                    throw new DefinedException(msg);
                }else{
                    String msg = String.format("Json data or schema verification success!");
                    LogHelper.info(msg);
                }
                return;
            }else{
                String msg = String.format("Validation methods %s are not currently supported. The list of supported method names is:%s", comparatorName, methodAlisaMap);
                throw new DefinedException(msg);
            }
        }
        LogHelper.debug("Expression: {}, The extracted value is : {}", exp, actual);
        String assertKeyInfo = String.format("%s%s%s%s%s%s%s", I18NFactory.getLocaleMessage("assert.check.point"), exp
                , I18NFactory.getLocaleMessage("assert.expect.value"), comparator.getExpect()
                , I18NFactory.getLocaleMessage("assert.actual.value"), actual
                , I18NFactory.getLocaleMessage("assert.check.result"));

        try {
            Class<?> clz = Class.forName("org.junit.Assert");
            Method method = clz.getMethod("assertThat", Object.class, Matcher.class);
            method.invoke(null, actual
                    , buildMatcherObj(comparatorName, methodAlisaMap.get(comparatorName), expect));
            LogHelper.info(assertKeyInfo + I18NFactory.getLocaleMessage("assert.check.result.pass"));
        } catch (ClassNotFoundException e) {
            String exceptionMsg = String.format("Class not found exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (NoSuchMethodException e) {
            String exceptionMsg = String.format("No such method exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (IllegalAccessException e) {
            String exceptionMsg = String.format("Illegal access exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (InvocationTargetException targetException) {
            LogHelper.error(assertKeyInfo + I18NFactory.getLocaleMessage("assert.check.result.fail"));
            throw new AssertionError(targetException.getCause());
        } catch (Exception e) {
            String exceptionMsg = String.format("Unknown exception occurs in the process of verifying data. Exception information: %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return;
    }

    /**
     * Build a comparator dynamically
     * @param objectMap Comparison object Meta
     * @return
     */
    private Comparator buildComparator(Map<String, Object> objectMap) {
       Comparator comparator = new Comparator();
        if (objectMap.containsKey(Constant.ASSERT_CHECK) && objectMap.containsKey(Constant.ASSERT_EXPECT)) {
            comparator = JSON.parseObject(JSON.toJSONString(objectMap), Comparator.class);
        } else {
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                comparator.setComparator(entry.getKey());
                Object objValue = entry.getValue();
                if (objValue instanceof List) {
                    List<Object> objValues = (List) objValue;
                    if (objValues.size() != 2) {
                        String exceptionMsg = "Verify that the expression is ill-formatted,correct Format eg: '-eq: [statusCode, 200]'";
                        throw new DefinedException(exceptionMsg);
                    }
                    comparator.setCheck(objValues.get(0));
                    comparator.setExpect(objValues.get(1));
                }
            }
        }
        return comparator;
    }


    /**
     * List with assertions
     *
     * @param mapList Assertion map list
     * @param responseEntity Response structure
     * @param env Current environment variable
     */
    public void assertList(List<Map<String, Object>> mapList, ResponseEntity responseEntity, Map<String, Object> env) {
        if (Objects.isNull(mapList)) {
            return;
        }
        for (Map<String, Object> objectMap : mapList) {
            assertObject(objectMap, responseEntity, env);
        }
    }

    /**
     *
     * @return Comparator method alias mapping relationship
     */
    public static Map<String, List> comparatorAlisaMap() {
        Map<String, List> methodMap = Maps.newHashMap();
        try {
            Class matcherClz = Class.forName("org.hamcrest.Matchers");
            Method[] methods = matcherClz.getDeclaredMethods();
            for (Method method : methods) {
                Type[] types = method.getParameterTypes();
                String methodName = method.getName();
                List<String> typeList = new ArrayList<>();
                for (Type type : types){
                    typeList.add(type.getTypeName());
                }
                methodMap.put(methodName,typeList);
                if (isSetAlisa(methodName)) {
                    String methodAlisa = transferMethodAlisa(methodName);
                    alisaMap.put(methodAlisa,methodName);
                    Integer parameterSize = typeList.size();
                    if(parameterSize == 1){
                        methodMap.put(methodAlisa,typeList);
                    }else {
                        String overrideMethodName = String.format("%s_%s",methodAlisa,parameterSize);
                        methodMap.put(overrideMethodName, typeList);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            String exceptionMsg = String.format("Class not found exception %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return methodMap;
    }

    /**
     * Non - built-in method aliases. A method name with more than 5 characters is not considered for aliases
     *
     * @param methodName
     * @return
     */
    private static boolean isSetAlisa(String methodName) {
        boolean flag = true;
        if (methodName.length() <= 5) {
            flag = false;
        }
        return flag;
    }

    /**
     * Parameter alias extraction rules with more than 5 characters
     * @param methodName  method of name
     * @return
     */
    private static String transferMethodAlisa(String methodName) {
        if (builtInAlisaMap.containsKey(methodName)) {
            return builtInAlisaMap.get(methodName);
        }
        StringBuilder methodAlisa = new StringBuilder();
        char[] chars = methodName.toCharArray();
        for (int index = 0; index < chars.length; index++) {
            char letter = chars[index];
            if (index == 0) {
                methodAlisa.append(letter);
            } else {
                if (Character.isUpperCase(letter)) {
                    methodAlisa.append(letter);
                }
            }
        }
        return methodAlisa.toString().toLowerCase();
    }

    static {
        builtInAlisaMap.put("equalTo", "eq");
        builtInAlisaMap.put("lessThan", "lt");
        builtInAlisaMap.put("lessThanOrEqualTo", "le");
        builtInAlisaMap.put("greaterThan", "gt");
        builtInAlisaMap.put("greaterThanOrEqualTo", "ge");
        builtInAlisaMap.put("not", "ne");
        /**
         * Extended JSON format verification
         */
        extendedJsonComparator.put("jsonValidate", Constant.JSON_VALIDATE);
        extendedJsonComparator.put("jv", Constant.JSON_VALIDATE);
        extendedJsonComparator.put("jsonSchemaValidate", Constant.JSON_SCHEMA_VALIDATE);
        extendedJsonComparator.put("jsv", Constant.JSON_SCHEMA_VALIDATE);
    }


}
