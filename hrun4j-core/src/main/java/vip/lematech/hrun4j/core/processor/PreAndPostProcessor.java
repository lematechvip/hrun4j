package vip.lematech.hrun4j.core.processor;

import cn.hutool.core.map.MapUtil;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.entity.base.BaseModel;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.TestStep;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.entity.http.ResponseEntity;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Pre - and post-processor
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
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
     * preprocessor
     * @param baseModel Based on the object
     * @param requestEntity The request entity
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
                LogHelper.info("[variables]：{}", transTestStep.getVariables());
            }
            if (outputs.contains("extract")) {
                LogHelper.info("[extract]：{}", this.testStepConfigVariable);
            }
        }
    }

    /**
     * post preprocess
     * @param obj The base model
     * @param responseEntity The response entity
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
