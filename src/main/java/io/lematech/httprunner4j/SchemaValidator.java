package io.lematech.httprunner4j;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SchemaValidator {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    /**
     *
     * @param testCaseJson
     * @return
     */
    public static void validateTestCaseValid(TestCase testCaseJson){
        JsonNode schemaNode = null;
        try {
            schemaNode = JsonLoader.fromResource(Constant.TEST_CASE_SCHEMA);
        } catch (IOException ioException) {
            String exceptionMsg = String.format("load resource {} io exception: %s",Constant.TEST_CASE_SCHEMA,ioException.getMessage());
            throw new DefinedException(exceptionMsg);
        }catch (Exception e){
            String exceptionMsg = String.format("load resource {} io exception: %s",Constant.TEST_CASE_SCHEMA,e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        JsonNode dataNode;
        try {
            dataNode = JsonLoader.fromString(JSON.toJSONString(testCaseJson));
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            ProcessingReport processingReport = schema.validate(dataNode);
            if(!processingReport.isSuccess()){
                String exceptionMsg = String.format("schema validate failure,exception: %s",processingReport.toString());
                throw new DefinedException(exceptionMsg);
            }
        } catch (IOException | ProcessingException ioException) {
            ioException.printStackTrace();
        }
    }


}
