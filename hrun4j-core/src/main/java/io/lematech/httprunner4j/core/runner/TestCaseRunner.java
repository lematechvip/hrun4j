package io.lematech.httprunner4j.core.runner;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.core.converter.ObjectConverter;
import io.lematech.httprunner4j.core.loader.Searcher;
import io.lematech.httprunner4j.core.loader.TestDataLoaderFactory;
import io.lematech.httprunner4j.core.processor.DataExtractor;
import io.lematech.httprunner4j.core.processor.PreAndPostProcessor;
import io.lematech.httprunner4j.core.validator.AssertChecker;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.entity.testcase.ApiModel;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.core.processor.ExpProcessor;
import io.lematech.httprunner4j.widget.i18n.I18NFactory;
import io.lematech.httprunner4j.widget.utils.HttpClientUtil;
import io.lematech.httprunner4j.widget.utils.RegExpUtil;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.collections.Maps;
import java.io.File;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestCaseDefined
 * @description Test case execution handler
 * @created 2021/1/20 11:07 上午
 * @publicWechat lematech
 */
public class TestCaseRunner {

    /**
     * expression processor
     */
    private ExpProcessor expProcessor;
    /**
     * testcase context variables
     */
    private Map<String, Object> testContextVariable;

    /**
     * seracher file by file path
     */
    private Searcher searcher;

    /**
     * assert checker
     */
    private AssertChecker assertChecker;

    /**
     * Pre - and post-processor
     */
    private PreAndPostProcessor preAndPostProcessor;

    /**
     * object converter
     */
    private ObjectConverter objectConverter;

    /**
     * data extractor
     */
    private DataExtractor dataExtractor;

    public TestCaseRunner() {
        this.expProcessor = new ExpProcessor();
        this.testContextVariable = Maps.newHashMap();
        this.searcher = new Searcher();
        this.assertChecker = new AssertChecker(expProcessor);
        this.preAndPostProcessor = new PreAndPostProcessor(expProcessor);
        this.objectConverter = new ObjectConverter();
        this.dataExtractor = new DataExtractor(expProcessor, testContextVariable);
    }

    /**
     * Actual execution of the test case logic
     */
    public void execute(TestCase testCase) {
        try {
            Config config = (Config) expProcessor.dynHandleContainsExpObject(testCase.getConfig());
            preAndPostProcessor.preProcess(config, new RequestEntity());
            List<TestStep> testSteps = testCase.getTestSteps();
            for (int index = 0; index < testSteps.size(); index++) {
                Map<String, Object> testStepConfigVariable = Maps.newHashMap();
                preAndPostProcessor.setTestStepConfigVariable(testStepConfigVariable);
                MyLog.info(I18NFactory.getLocaleMessage("runner.current.step") + " : {}", testSteps.get(index).getName());
                Map configVariables = Objects.isNull(config) ? Maps.newHashMap() : (Map) config.getVariables();
                TestStep testStep = referenceApiModelOrTestCase(testSteps.get(index), configVariables);
                RequestEntity initializeRequestEntity = testStep.getRequest();
                if (Objects.isNull(initializeRequestEntity)) {
                    continue;
                }
                preAndPostProcessor.preProcess(testStep, initializeRequestEntity);
                expProcessor.setVariablePriority(testStepConfigVariable, testContextVariable, configVariables, (Map) testStep.getVariables());
                RequestEntity requestEntity = (RequestEntity) expProcessor.dynHandleContainsExpObject(initializeRequestEntity);
                requestEntity.setUrl(getUrl(config.getBaseUrl(), testStep.getRequest().getUrl()));
                ResponseEntity responseEntity = HttpClientUtil.executeReq(requestEntity);
                preAndPostProcessor.postProcess(testStep, responseEntity);
                List<Map<String, Object>> validateList = testStep.getValidate();
                assertChecker.assertList(validateList, responseEntity, testStepConfigVariable);
                dataExtractor.extractVariables(testStep.getExtract(), responseEntity, testStepConfigVariable);
            }
            preAndPostProcessor.postProcess(config, new ResponseEntity());
        } catch (DefinedException definedException) {
            throw definedException;
        } catch (Exception e) {
            e.printStackTrace();
            String exceptionMsg = String.format("Unknown exception occurred in test case  execution. Exception information:%s", e.getMessage());
            MyLog.debug("Unknown exception occurred in test case  execution. Exception information:{}", e.getStackTrace());
            throw new DefinedException(exceptionMsg);
        }

    }
    /**
     * extend api model properties value
     *
     * @param testStep
     * @return
     */
    private TestStep referenceApiModelOrTestCase(TestStep testStep, Map variables) {
        String testcase = testStep.getTestcase();
        if (!StrUtil.isEmpty(testcase)) {
            String spliceCaseFilePath = searcher.spliceFilePath(testcase, Constant.TEST_CASE_DIRECTORY_NAME);
            File testCasePath = searcher.quicklySearchFile(spliceCaseFilePath);
            /**
             * config variables can express to reference testcases
             */
            TestCase testCase = TestDataLoaderFactory.getLoader(FileUtil.extName(testcase)).load(testCasePath, TestCase.class);
            Config tcConfig = testCase.getConfig();
            Object referenceCaseVariables = tcConfig.getVariables();
            if (referenceCaseVariables instanceof Map) {
                tcConfig.setVariables(objectConverter.mapExtendsKeyValue(variables, (Map) referenceCaseVariables));
            } else {
                MyLog.warn("Reference test case {}, configuration variable type is not Map type", testcase);
            }
            this.execute(testCase);
        }
        String api = testStep.getApi();
        if (!StrUtil.isEmpty(api)) {
            String spliceApiFilePath = Searcher.spliceFilePath(api, Constant.API_DEFINE_DIRECTORY_NAME);
            File apiFilePath = searcher.quicklySearchFile(spliceApiFilePath);
            ApiModel apiModel = TestDataLoaderFactory.getLoader(FileUtil.extName(apiFilePath)).load(apiFilePath, ApiModel.class);
            TestStep extendTestStep = (TestStep) objectConverter.objectsExtendsPropertyValue(testStep, objectConverter.apiModel2TestStep(apiModel));
            MyLog.debug("Interface documentation information:{}, Test steps:{}, After merging:{}", JSON.toJSONString(apiModel), JSON.toJSONString(testStep), JSON.toJSONString(extendTestStep));
            return extendTestStep;
        }
        return testStep;
    }

    /**
     * get url
     *
     * @param baseUrl
     * @param requestUrl
     * @return
     */
    private String getUrl(String baseUrl, String requestUrl) {
        if (RegExpUtil.isUrl(requestUrl)) {
            return requestUrl;
        }
        return String.format("%s%s", baseUrl, StrUtil.isEmpty(requestUrl) ? "" : requestUrl);
    }


}
