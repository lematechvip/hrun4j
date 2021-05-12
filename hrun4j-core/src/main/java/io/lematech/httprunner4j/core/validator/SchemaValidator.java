package io.lematech.httprunner4j.core.validator;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestSuite;

import java.io.IOException;
import java.util.Iterator;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className SchemaValidator
 * @description validator schemas
 * @created 2021/1/22 4:07 下午
 * @publicWechat lematech
 */

public class SchemaValidator {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    /**
     * Verify that the object file format meets the requirements
     *
     * @param clz
     * @param obj
     */
    public static String validateJsonObjectFormat(Class clz, Object obj) {
        JsonNode schemaNode;
        String jsonFormatSchema;
        if (clz == TestCase.class) {
            jsonFormatSchema = Constant.TEST_CASE_SCHEMA;
        } else if (clz == ApiModel.class) {
            jsonFormatSchema = Constant.API_MODEL_SCHEMA;
        } else if (clz == TestSuite.class) {
            jsonFormatSchema = Constant.TEST_SUITE_SCHEMA;
        } else {
            String exceptionMsg = String.format("Current class %s format validation is not currently supported", clz);
            throw new DefinedException(exceptionMsg);
        }
        try {
            schemaNode = JsonLoader.fromResource(jsonFormatSchema);
        } catch (IOException ioException) {
            String exceptionMsg = String.format("Error in loading schema file %s. Exception message: %s", jsonFormatSchema, ioException.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        JsonNode dataNode;
        try {
            dataNode = JsonLoader.fromString(JSON.toJSONString(obj));
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            ProcessingReport processingReport = schema.validate(dataNode);
            if (!processingReport.isSuccess()) {
                StringBuffer errorInfo = new StringBuffer();
                Iterator<ProcessingMessage> itr = processingReport.iterator();
                while (itr.hasNext()) {
                    ProcessingMessage message = itr.next();
                    if (message.getLogLevel().equals(LogLevel.ERROR)) {
                        errorInfo.append(message);
                    }
                }
                String exceptionMsg = String.format("Class: %s,object: %s,JSON data validation failed because:: %s,", clz.getSimpleName(), JSON.toJSON(obj), errorInfo.toString());
                return exceptionMsg;
            }
        } catch (IOException | ProcessingException ioException) {
            String exceptionMsg = String.format("By trying to verify that the JSON exception has occurred, the exception message is:%s", ioException.getMessage());
            return exceptionMsg;
        }
        return null;
    }

}
