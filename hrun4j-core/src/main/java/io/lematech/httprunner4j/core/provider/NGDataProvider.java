package io.lematech.httprunner4j.core.provider;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.NamespaceMap;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.loader.Searcher;
import io.lematech.httprunner4j.core.loader.TestDataLoaderFactory;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import io.lematech.httprunner4j.widget.utils.SmallUtil;
import org.testng.collections.Maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className NGDataProvider
 * @description testng data provider and Complete data-driven grouping
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */

public class NGDataProvider {
    private Searcher searcher;
    private DataConstructor dataConstructor;
    public NGDataProvider() {
        searcher = new Searcher();
        dataConstructor = new DataConstructor();
    }

    /**
     * datq provider implement
     *
     * @param pkgName
     * @param testCaseName
     * @return
     */
    public Object[][] dataProvider(String pkgName, String testCaseName) {
        TestCase testCase;
        if (RunnerConfig.getInstance().getRunMode() == RunnerConfig.RunMode.CLI) {
            String namespace = getNamespace(pkgName, testCaseName);
            testCase = NamespaceMap.getDataObject(String.format("%s:%s", RunnerConfig.RunMode.CLI, namespace));
        } else {
            File dataFilePath = searcher.quicklySearchFile(caseFilePath(pkgName, testCaseName));
            String extName = RunnerConfig.getInstance().getTestCaseExtName();
            testCase = TestDataLoaderFactory.getLoader(extName)
                    .load(dataFilePath, TestCase.class);
        }
        if (Objects.isNull(testCase)) {
            String exceptionMsg = String.format("According to the current running mode %s and matching rules [pkgName:%s,caseName:%s], find the use case data is empty"
                    , RunnerConfig.getInstance().getRunMode(), pkgName, testCaseName);
            throw new DefinedException(exceptionMsg);
        }
        Object[][] testCases = getObjects(testCase);
        return testCases;
    }

    /**
     * package name and testcase name transfer to file path name
     *
     * @param pkgName
     * @param testCaseName
     * @return
     */
    private String caseFilePath(String pkgName, String testCaseName) {
        if (pkgName.startsWith(definePackageName)) {
            pkgName = FilesUtil.pkgPath2DirPath(pkgName.replaceFirst(definePackageName, ""));
            if (pkgName.startsWith(Constant.UNDERLINE)) {
                pkgName = pkgName.replaceFirst(Constant.UNDERLINE, Constant.DOT_PATH);
            }
        } else {
            pkgName = FilesUtil.pkgPath2DirPath(pkgName);
        }
        return pkgName + File.separator + testCaseName;
    }

    String definePackageName = RunnerConfig.getInstance().getPkgName();

    /**
     * Get the namespace by package name and method name
     *
     * @param pkgName
     * @param testCaseName
     * @return
     */
    private String getNamespace(String pkgName, String testCaseName) {
        StringBuffer namespace = new StringBuffer();
        if (pkgName.startsWith(definePackageName)) {
            pkgName = pkgName.replaceFirst(definePackageName, "");
            if (pkgName.startsWith(Constant.UNDERLINE)) {
                pkgName = pkgName.replaceFirst(Constant.UNDERLINE, "");
            }
            if (pkgName.startsWith(Constant.DOT_PATH)) {
                pkgName = pkgName.replaceFirst(Constant.DOT_PATH, "");
            }
        } else {
            pkgName = FilesUtil.pkgPath2DirPath(pkgName);
        }
        namespace.append(JavaIdentifierUtil.formatFilePath(pkgName));
        namespace.append(Constant.UNDERLINE);
        namespace.append(testCaseName);
        return namespace.toString();
    }

    private Object[][] getObjects(TestCase testCase) {
        Object[][] testCases;
        Object parameterValues = testCase.getConfig().getParameters();
        List<Map<String, Object>> parameters = dataConstructor.parameterized(parameterValues);
        if (Objects.isNull(parameters) || parameters.size() == 0) {
            testCases = new Object[1][];
            testCases[0] = new Object[]{testCase};
            return testCases;
        }
        List<TestCase> result = new ArrayList<>();
        Object configVariables = testCase.getConfig().getVariables();
        if (Objects.isNull(configVariables)) {
            configVariables = Maps.newHashMap();
        }
        if (!(configVariables instanceof Map)) {
            String exceptionMsg = String.format("Use case configuration variable types can only be key-value pair types; they cannot be %s", configVariables.getClass());
            throw new DefinedException(exceptionMsg);
        }
        for (Map<String, Object> parameterVariables : parameters) {
            Map resultVariables = Maps.newHashMap();
            resultVariables.putAll((Map) configVariables);
            resultVariables.putAll(MapUtil.isEmpty(parameterVariables) ? Maps.newHashMap() : parameterVariables);
            TestCase copyTestCase = (TestCase) SmallUtil.objectDeepCopy(testCase, TestCase.class);
            Config config = copyTestCase.getConfig();
            config.setVariables(resultVariables);
            config.setParameters(parameterVariables);
            copyTestCase.setConfig(config);
            result.add(copyTestCase);
        }
        testCases = new Object[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            testCases[i] = new Object[]{result.get(i)};
        }
        return testCases;
    }

}
