package vip.lematech.hrun4j.helper;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.config.i18n.I18NFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class JsonHelper {
    private static JmesPath<JsonNode> jmespath = new JacksonRuntime();
    private static BaseResult baseResult;
    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
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
        if (StrUtil.isEmpty(jsonStr)) {
            return false;
        }
        boolean isJsonObject = true;
        boolean isJsonArray = true;
        try {
            JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            isJsonObject = false;
        }
        try {
            JSONObject.parseArray(jsonStr);
        } catch (Exception e) {
            isJsonArray = false;
        }
        return isJsonObject || isJsonArray;
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
            LogHelper.info("JSON write file exception, exception information: %s", e.getMessage());
        }
    }



    /**
     * Update results, error count
     * @param resultDetail
     */
    private static void updateBaseResult(ResultDetail resultDetail) {
        baseResult.setWrongNumber(baseResult.getWrongNumber() + 1);
        if (baseResult.getResultDetail() == null) {
            ArrayList<ResultDetail> resultDetailArrayList = new ArrayList<ResultDetail>();
            resultDetailArrayList.add(resultDetail);
            baseResult.setResultDetail(resultDetailArrayList);
        } else {
            baseResult.getResultDetail().add(resultDetail);
        }
    }

    /**
     * String Compare
     * @param actualString
     * @param expectString
     * @param key
     * @param prefix
     * @param ignore
     */
    private static void jsonValidate(String actualString, String expectString, String key, String prefix , String ignore) {
        boolean status = false;
        if (StringUtils.isNotBlank(ignore)) {
            List<String> listIgnore = Arrays.asList(ignore.split(","));
            status = listIgnore.contains(key);
        }
        if (!status) {
            if (!actualString.equals(expectString)) {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual(actualString)
                        .expect(expectString)
                        .msg("Value Not Equal")
                        .prefix(prefix)
                        .build();
                updateBaseResult(resultDetail);
                LogHelper.info(String.format(I18NFactory.getLocaleMessage("json.compare.fail"), prefix,expectString, actualString));
            } else {
                LogHelper.info(String.format(I18NFactory.getLocaleMessage("json.compare.success"), prefix,expectString, actualString));
            }
        }
    }

    /**
     * JSONObject Compare
     * @param actualJson
     * @param expectJson
     * @param key
     * @param prefix
     * @param ignore
     */
    private static void jsonValidate(JSONObject actualJson, JSONObject expectJson, String key, String prefix, String ignore) {
        if (StringUtils.isBlank(prefix)){
            prefix = "";
        }else {
            prefix = prefix + ".";
        }
        for (String s : expectJson.keySet()) {
            key = s;
            jsonValidate(actualJson.get(key), expectJson.get(key), key, prefix + key, ignore);
        }
    }

    /**
     * JsonArray Compare
     * @param actualJsonArray
     * @param expectJsonArray
     * @param key
     * @param prefix
     * @param ignore
     */
    private static void jsonValidate(JSONArray actualJsonArray, JSONArray expectJsonArray, String key, String prefix, String ignore) {
        if (Objects.nonNull(actualJsonArray) && Objects.nonNull(expectJsonArray)) {
            if (actualJsonArray.size() == expectJsonArray.size()) {
                Iterator iteratorActualJsonArray = actualJsonArray.iterator();
                if (StringUtils.isBlank(prefix)){
                    prefix = "";
                }
                int num = 0;
                for (Object o : expectJsonArray) {
                    jsonValidate(iteratorActualJsonArray.next(), o, key,prefix+"["+num+"]", ignore);
                    num ++;
                }
            } else {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual(actualJsonArray)
                        .prefix(prefix)
                        .expect(expectJsonArray)
                        .msg("Length Not Equal")
                        .build();
                updateBaseResult(resultDetail);
            }
        } else {
            if (Objects.isNull(actualJsonArray) && Objects.isNull(expectJsonArray)) {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual("Not Exist in actualJsonArray")
                        .expect("Not Exist in expectJsonArray")
                        .msg("Both Not Exist")
                        .build();
                updateBaseResult(resultDetail);
            } else if (Objects.isNull(actualJsonArray)) {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual("Not Exist in actualJsonArray")
                        .expect("Not Exist in expectJsonArray")
                        .msg("Other Exist")
                        .build();
                updateBaseResult(resultDetail);
            } else {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual(actualJsonArray)
                        .prefix(prefix)
                        .expect("Not Exist in expectJsonArray")
                        .msg("Other Exist")
                        .build();
                updateBaseResult(resultDetail);
            }
        }

    }

    /**
     * Object Compare
     * @param actualJson
     * @param expectJson
     * @param key
     * @param prefix
     * @param ignore
     */
    private static void jsonValidate(Object actualJson, Object expectJson, String key,String prefix, String ignore) {
        if (Objects.nonNull(actualJson) && Objects.nonNull(expectJson)) {
            if (actualJson instanceof JSONObject) {
                jsonValidate((JSONObject)actualJson, (JSONObject)expectJson, key,prefix, ignore);
            } else if (actualJson instanceof JSONArray) {
                jsonValidate((JSONArray)actualJson, (JSONArray)expectJson, key,prefix, ignore);
            } else if (actualJson instanceof String) {
                try {
                    String actualJsonToStr = actualJson.toString();
                    String expectJsonToStr = expectJson.toString();
                    jsonValidate(actualJsonToStr, expectJsonToStr, key,prefix, ignore);
                } catch (Exception e) {
                    ResultDetail resultDetail = ResultDetail.builder()
                            .actual(actualJson)
                            .prefix(prefix)
                            .expect(expectJson)
                            .msg("An exception occurred during String conversion")
                            .build();
                    updateBaseResult(resultDetail);
                }
            } else {
                jsonValidate(actualJson.toString(), expectJson.toString(), key,prefix, ignore);
            }
        } else {
            if (Objects.isNull(actualJson) && Objects.isNull(expectJson)) {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual("Not Exist in actualJson")
                        .prefix(prefix)
                        .expect("Not Exist in expectJson")
                        .msg("Both Not Exist")
                        .build();
                updateBaseResult(resultDetail);
            } else if (Objects.isNull(actualJson) ) {
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual("Not Exist in actualJson")
                        .prefix(prefix)
                        .expect(expectJson)
                        .msg("Other Exist")
                        .build();
                updateBaseResult(resultDetail);
            } else{
                ResultDetail resultDetail = ResultDetail.builder()
                        .actual(actualJson)
                        .prefix(prefix)
                        .expect("Not Exist in expectJson")
                        .msg("Not Exist")
                        .build();
                updateBaseResult(resultDetail);
            }

        }

    }

    /**
     * jsonValidateFactory
     * @param actual
     * @param expect
     * @param ignore
     * @param <T>
     * @return
     */
    public static <T> BaseResult jsonValidateFactory(String comparator,T actual, T expect, String ignore) {
        baseResult = new BaseResult();
        if(comparator.equals(Constant.JSON_VALIDATE)){
            jsonValidate(actual, expect, null,"$", ignore);
        }else if(comparator.equals(Constant.JSON_SCHEMA_VALIDATE)){
            jsonSchemaValidate(actual,expect);
        }
        return baseResult;
    }

    @Data
    public static class BaseResult {
        private int wrongNumber;
        private ArrayList<ResultDetail> resultDetail = new ArrayList<>();
    }

    @Data
    @Builder
    public static class ResultDetail {
        private String prefix;
        private Object actual;
        private Object expect;
        private String msg;
        public ResultDetail(String prefix, Object actual, Object expect, String msg) {
            this.prefix = prefix;
            this.actual = actual;
            this.expect = expect;
            this.msg = msg;
        }
    }

    private static void jsonSchemaValidate(Object schema,Object jsonData){
        try {
            JsonNode schemaNode = JsonLoader.fromString(JSON.toJSONString(schema));
            JsonNode dataNode = JsonLoader.fromString(JSON.toJSONString(jsonData));
            ProcessingReport processingReport = factory.getJsonSchema(schemaNode).validate(dataNode);
            if (!processingReport.isSuccess()) {
                StringBuilder errorInfo = new StringBuilder();
                Iterator<ProcessingMessage> itr = processingReport.iterator();
                while (itr.hasNext()) {
                    ProcessingMessage message = itr.next();
                    if (message.getLogLevel().equals(LogLevel.ERROR)) {
                        ResultDetail resultDetail = ResultDetail.builder()
                                .msg(message.toString())
                                .build();
                        updateBaseResult(resultDetail);
                    }
                }
                String exceptionMsg = String.format("Json Schema Validate failed，Exception message: %s", errorInfo);
                throw new DefinedException(exceptionMsg);
            }
        } catch (IOException ioException) {
            String exceptionMsg =String.format("Failed to load the JSON Schema Or Data, Exception message: %s", ioException.getMessage());
           throw new DefinedException(exceptionMsg);
        } catch (ProcessingException processingException) {
            String exceptionMsg = String.format("Generate Schema objects by schema exception has occurred, Exception message is:%s", processingException.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }
}
