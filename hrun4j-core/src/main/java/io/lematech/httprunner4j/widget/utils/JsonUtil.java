package io.lematech.httprunner4j.widget.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.log.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class JsonUtil {
    private static JmesPath<JsonNode> jmespath = new JacksonRuntime();

    /**
     * get value by jmespath
     *
     * @param exp            expression
     * @param responseEntity The response entity
     * @return Extracted data
     */
    public static Object getJmesPathResult(String exp, String responseEntity) {
        Object dataExtractorValue;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(responseEntity);
            Expression<JsonNode> compileExp = jmespath.compile(exp);
            JsonNode jsonNode = compileExp.search(actualObj);
            dataExtractorValue = getJsonNodeValue(jsonNode);
            /**
             * jmespath 0.5 bug jsonNode not null,is "null" To fix
             */
            if (Objects.isNull(dataExtractorValue) || Constant.DATA_EXTRACTOR_JMESPATH_NODE_NULL.equals(jsonNode.asText())) {
                if (exp.startsWith(Constant.DATA_EXTRACTOR_JMESPATH_Content_START + Constant.DOT_PATH) ||
                        exp.startsWith(Constant.DATA_EXTRACTOR_JMESPATH_HEADERS_START + Constant.DOT_PATH)) {
                    return null;
                }
                return exp;
            }
        } catch (Exception e) {
            if (exp.startsWith(Constant.DATA_EXTRACTOR_JMESPATH_Content_START + Constant.DOT_PATH)) {
                return null;
            }
            /**
             * jmespath 0.5 bug  Unable to compile expression "headers.Content-Type": syntax error token recognition error at: '-T'
             */
            if (exp.startsWith(Constant.DATA_EXTRACTOR_JMESPATH_HEADERS_START + Constant.DOT_PATH)) {
                if (exp.contains(Constant.PARAMETER_SEPARATOR)) {
                    JSONObject responseJson = JSONObject.parseObject(responseEntity);
                    JSONObject headersJson = responseJson.getJSONObject(Constant.DATA_EXTRACTOR_JMESPATH_HEADERS_START);
                    String[] headerMetas = exp.split(Constant.DOT_ESCAPE_PATH);
                    if (headerMetas.length == 2) {
                        return headersJson.get(headerMetas[1]);
                    } else {
                        String exceptionMsg = String.format("Jmespath does not support the current expression %s, please use jsonpath", exp);
                        throw new DefinedException(exceptionMsg);
                    }
                }
                return null;
            }
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
     * @param exp  The expression
     * @param respStr The response string
     * @return result of expression
     */
    public static String getJsonPathResult(String exp, String respStr) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(respStr);
        String searchResult = JsonPath.read(document, exp);
        return searchResult;
    }

    /**
     * is json
     *
     * @param jsonStr json string
     * @return true if json
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

    public static void jsonWriteToFile(File jsonFilePath, Object json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(json);
            OutputStream out = new FileOutputStream(jsonFilePath);
            byte[] b = value.getBytes();
            for (int i = 0; i < b.length; i++) {
                out.write(b[i]);
            }
            out.close();
        } catch (IOException e) {
            MyLog.info("JSON write file exception, exception information: %s", e.getMessage());
        }
    }
}
