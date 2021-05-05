package io.lematech.httprunner4j.core.processor;

import cn.hutool.core.map.MapUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.base.BaseModel;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className PreAndPostProcessor
 * @description Pre - and post-processor
 * @created 2021/4/25 10:36 下午
 * @publicWechat lematech
 */
public class PreAndPostProcessor {
    public void setTestStepConfigVariable(Map<String, Object> testStepConfigVariable) {
        this.testStepConfigVariable = testStepConfigVariable;
    }

    /**
     * teststep context variables
     */
    private Map<String, Object> testStepConfigVariable;

    /**
     * expression processor
     */
    private ExpProcessor expProcessor;

    public PreAndPostProcessor(ExpProcessor expProcessor) {
        this.expProcessor = expProcessor;
    }

    /**
     * pre preprocess
     */
    public void preProcess(BaseModel baseModel, RequestEntity requestEntity) {
        if (baseModel instanceof TestStep && !Objects.isNull(requestEntity)) {
            this.testStepConfigVariable.put(Constant.REQUEST_VARIABLE_NAME, requestEntity);
        }
        if (Objects.isNull(baseModel)) {
            String exceptionMsg = String.format("BaseModel object cannot be null");
            throw new DefinedException(exceptionMsg);
        }
        expProcessor.handleVariables2Map(baseModel);
        Object hookObj = baseModel.getSetupHooks();
        if (Objects.isNull(hookObj)) {
            return;
        }
        process(baseModel, hookObj);

    }

    /**
     * @param transTestStep
     */
    private void outputVariables(TestStep transTestStep) {
        List outputs = transTestStep.getOutput();
        if (!Objects.isNull(outputs)) {
            if (outputs.contains("variables")) {
                MyLog.info("输出变量[variables]：{}", transTestStep.getVariables());
            }
            if (outputs.contains("extract")) {
                MyLog.info("输出变量[extract]：{}", this.testStepConfigVariable);
            }
        }
    }

    /**
     * post preprocess
     */
    public void postProcess(BaseModel obj, ResponseEntity responseEntity) {
        if (obj instanceof TestStep && !Objects.isNull(responseEntity)) {
            testStepConfigVariable.put(Constant.RESPONSE_VARIABLE_NAME, responseEntity);
        }
        Object hookMeta = obj.getTeardownHooks();
        if (Objects.isNull(hookMeta)) {
            return;
        }
        process(obj, hookMeta);
        if (obj instanceof TestStep) {
            outputVariables((TestStep) obj);
        }
    }

    /**
     * process
     *
     * @param baseModel
     * @param hookMeta
     */
    private void process(BaseModel baseModel, Object hookMeta) {
        Map result = expProcessor.handleHookExp(hookMeta);
        Map variablesMap = Maps.newHashMap();
        variablesMap.putAll(MapUtil.isEmpty((Map) baseModel.getVariables()) ? Maps.newHashMap() : (Map) baseModel.getVariables());
        variablesMap.putAll(MapUtil.isEmpty(result) ? Maps.newHashMap() : result);
        baseModel.setVariables(variablesMap);
    }
}
