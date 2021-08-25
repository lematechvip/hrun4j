package vip.lematech.hrun4j.core.provider;

import cn.hutool.core.map.MapUtil;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.config.NamespaceMap;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.entity.testcase.Config;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.helper.LittleHelper;
import vip.lematech.hrun4j.core.loader.Searcher;
import vip.lematech.hrun4j.core.loader.TestDataLoaderFactory;
import org.testng.collections.Maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * testng data provider and Complete data-driven grouping
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
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
     * @param pkgName The package name
     * @param testCaseName The test case name
     * @return The data provider
     */
    public Object[][] dataProvider(String pkgName, String testCaseName) {
        TestCase testCase = null;
        if (RunnerConfig.getInstance().getRunMode() == RunnerConfig.RunMode.CLI) {
            String namespace = getNamespace(pkgName, testCaseName);
            testCase = NamespaceMap.getDataObject(String.format("%s:%s", RunnerConfig.RunMode.CLI, namespace));
        } else if(RunnerConfig.getInstance().getRunMode() == RunnerConfig.RunMode.POM){
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
            pkgName = FilesHelper.pkgPath2DirPath(pkgName.replaceFirst(definePackageName, ""));
            if (pkgName.startsWith(Constant.UNDERLINE)) {
                pkgName = pkgName.replaceFirst(Constant.UNDERLINE, Constant.DOT_PATH);
            }
        } else {
            pkgName = FilesHelper.pkgPath2DirPath(pkgName);
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
            pkgName = FilesHelper.pkgPath2DirPath(pkgName);
        }
        namespace.append(JavaIdentifierHelper.formatFilePath(pkgName));
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
            TestCase copyTestCase = (TestCase) LittleHelper.objectDeepCopy(testCase, TestCase.class);
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
