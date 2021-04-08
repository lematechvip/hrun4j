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
import io.lematech.httprunner4j.entity.base.BaseModel;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class SchemaValidator {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    /**
     * @param clz
     * @param obj
     */
    public static String validateJsonObjectFormat(Class clz, Object obj) {
        JsonNode schemaNode;
        String jsonFormatSchema = "";
        try {
            if (clz == TestCase.class) {
                jsonFormatSchema = Constant.TEST_CASE_SCHEMA;
            } else if (clz == ApiModel.class) {
                jsonFormatSchema = Constant.API_MODEL_SCHEMA;
            } else {
                String exceptionMsg = String.format("not support %s class validate: %s", clz);
                throw new DefinedException(exceptionMsg);
            }
            schemaNode = JsonLoader.fromResource(jsonFormatSchema);
        } catch (IOException ioException) {
            String exceptionMsg = String.format("load resource %s io exception: %s", jsonFormatSchema, ioException.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (Exception e) {
            String exceptionMsg = String.format("load resource %s io exception: %s", jsonFormatSchema, e.getMessage());
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
                String exceptionMsg = String.format("schema validate failure: %s,clz: %s,object: %s", errorInfo.toString(), clz.getSimpleName(), JSON.toJSON(obj));
                return exceptionMsg;
            }
        } catch (IOException | ProcessingException ioException) {
            String exceptionMsg = String.format("schema validate data error:%s", ioException);
            return exceptionMsg;
        }
        return null;
    }

}
