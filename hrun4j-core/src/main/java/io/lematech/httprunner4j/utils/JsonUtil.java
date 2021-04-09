package io.lematech.httprunner4j.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JsonUtil
 * @description TODO
 * @created 2021/2/3 5:57 下午
 * @publicWechat lematech
 */
public class JsonUtil {
    private static JmesPath<JsonNode> jmespath = new JacksonRuntime();

    /**
     * get value by jmespath
     *
     * @param exp
     * @param jsonobj
     * @return
     */
    public static JsonNode getJmesPathResult(String exp, String jsonobj) {
        JsonNode jsonResult = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(jsonobj);
            Expression<JsonNode> compileExp = jmespath.compile(exp);
            jsonResult = compileExp.search(actualObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * get value by jsonpath
     *
     * @param exp
     * @param respStr
     * @return
     */
    public static String getJsonPathResult(String exp, String respStr) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(respStr);
        String searchResult = JsonPath.read(document, exp);
        return searchResult;
    }

    /**
     * is json
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJson(String jsonStr) {
        boolean flag = true;
        try {
            JSON.parseObject(jsonStr);

        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
