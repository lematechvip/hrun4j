package io.lematech.httprunner4j.widget.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;

import java.util.Objects;

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
     * @param responseEntity
     * @return
     */
    public static Object getJmesPathResult(String exp, String responseEntity) {
        Object dataExtractorValue;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(responseEntity);
            Expression<JsonNode> compileExp = jmespath.compile(exp);
            JsonNode jsonNode = compileExp.search(actualObj);
            dataExtractorValue = getJsonNodeValue(jsonNode);
            if (Objects.isNull(dataExtractorValue) || "null".equals(jsonNode.asText())) {
                return exp;
            }
        } catch (Exception e) {
            dataExtractorValue = exp;
        }
        return dataExtractorValue;
    }

    /**
     * Gets the data value of the node
     *
     * @param jsonNode
     * @return
     */
    private static Object getJsonNodeValue(JsonNode jsonNode) {
        if (jsonNode.isBoolean()) {
            return jsonNode.asBoolean();
        } else if (jsonNode.isDouble() || jsonNode.isFloat()) {
            return jsonNode.asDouble();
        } else if (jsonNode.isInt()) {
            return jsonNode.asInt();
        } else {
            return jsonNode.asText();
        }
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
