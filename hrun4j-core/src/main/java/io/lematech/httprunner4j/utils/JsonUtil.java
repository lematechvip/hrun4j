package io.lematech.httprunner4j.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Slf4j
public class JsonUtil {
    private static JmesPath<JsonNode> jmespath = new JacksonRuntime();
    public static JsonNode getJmesPathResult(String expression,String jsonobj){
        JsonNode jsonResult = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(jsonobj);
            //log.info("转化结构：{}", jsonobj);
            Expression<JsonNode> compileExp = jmespath.compile(expression);
            jsonResult = compileExp.search(actualObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }
}
