package vip.lematech.hrun4j.core.processor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import vip.lematech.hrun4j.helper.JsonHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.helper.RegExpHelper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.entity.http.ResponseEntity;

import java.util.*;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class DataExtractor {

    private ExpProcessor expProcessor;
    private Map testContextVariable;

    public DataExtractor(ExpProcessor expProcessor) {
        this.expProcessor = expProcessor;
    }

    public DataExtractor(ExpProcessor expProcessor, Map testContextVariable) {
        this.expProcessor = expProcessor;
        this.testContextVariable = testContextVariable;
    }

    /**
     * Support regular, JSONPATH, JMESPATH, expression and other ways to extract parameters
     *
     * @param exp            expression language
     * @param responseEntity response entity
     * @return The object after processing
     */
    public Object handleExpDataExtractor(Object exp, ResponseEntity responseEntity) {
        if (!(exp instanceof String) || Objects.isNull(exp)) {
            return exp;
        }
        String expStr = String.valueOf(exp);
        if (StrUtil.isEmpty(expStr)) {
            return "";
        }
        Object dataExtractorValue;
        String responseStr = JSON.toJSONString(responseEntity);
        if (RegExpHelper.isExp(expStr)) {
            dataExtractorValue = expProcessor.handleStringExp(expStr);
        } else if (expStr.startsWith(Constant.DATA_EXTRACTOR_REGEX_START) && expStr.endsWith(Constant.DATA_EXTRACTOR_REGEX_END)) {
            String expression = expStr.substring(1, expStr.length() - 1);
            dataExtractorValue = RegExpHelper.findString(expression, responseStr);
        } else if (expStr.startsWith(Constant.DATA_EXTRACTOR_JSONPATH_START)) {
            dataExtractorValue = JsonHelper.getJsonPathResult(expStr, responseStr);
        } else {
            dataExtractorValue = JsonHelper.getJmesPathResult(expStr, responseStr);
        }
        if (Objects.isNull(dataExtractorValue)) {
            String exceptionMsg = String.format("No data was found by the given matching pattern: %s", exp);
            throw new DefinedException(exceptionMsg);
        }
        return dataExtractorValue;
    }


    /**
     * Extract data according to the extraction rules
     * @param extracts extracted data
     * @param responseEntity The response entity
     * @param variable variable map
     */
    public void extractVariables(Object extracts, ResponseEntity responseEntity, Map<String, Object> variable) {
        if (Objects.isNull(extracts)) {
            return;
        }
        Class clz = extracts.getClass();
        LogHelper.debug("Data extractor type : {}", clz);
        if (clz == ArrayList.class) {
            List<Map<String, String>> extractList = (List<Map<String, String>>) extracts;
            for (Map extractMap : extractList) {
                extractMap(responseEntity, extractMap, variable);
            }
        } else if (clz == Map.class || clz == LinkedHashMap.class) {
            Map extractMap = (Map) extracts;
            extractMap(responseEntity, extractMap, variable);
        } else {
            String exceptionMsg = String.format("The current extractor of this type %s is not supported", clz);
            throw new DefinedException(exceptionMsg);
        }
    }

    /**
     * Extract as a key-value pair
     *
     * @param responseEntity
     * @param extractMap
     * @param testStepConfigVariable
     */
    private void extractMap(ResponseEntity responseEntity, Map<String, Object> extractMap, Map<String, Object> testStepConfigVariable) {
        for (Map.Entry<String, Object> entry : extractMap.entrySet()) {
            String key = entry.getKey();
            if (Objects.isNull(entry.getValue())) {
                String exceptionMsg = String.format("The data extraction rule cannot be empty");
                throw new DefinedException(exceptionMsg);
            }
            Object expValue = entry.getValue();
            Object extractValue = handleExpDataExtractor(expValue, responseEntity);
            if (extractValue.equals(expValue)) {
                String exceptionMsg = String.format("By extracting the data that the rule %s does not match to the rule", expValue);
                throw new DefinedException(exceptionMsg);
            }
            LogHelper.debug("Extract rule %s, the extracted data value:%s", expValue, extractValue);
            testContextVariable.put(key, extractValue);
        }

    }
}
