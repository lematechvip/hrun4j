package io.lematech.httprunner4j.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JsonUtil
 * @description TODO
 * @created 2021/2/3 5:57 下午
 * @publicWechat lematech
 */
public class JsonUtil {
    private static JmesPath<JsonNode> jmespath = new JacksonRuntime();;
    public static JsonNode getJmesPathResult(String expression,String jsonInput){
        JsonNode jsonResult = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(jsonInput);
            Expression<JsonNode> compileExp = jmespath.compile(expression);
            jsonResult = compileExp.search(actualObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }
}
