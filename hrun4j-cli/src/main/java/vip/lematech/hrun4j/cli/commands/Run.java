package vip.lematech.hrun4j.cli.commands;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.helper.LittleHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.cli.testsuite.TestNGEngine;
import vip.lematech.hrun4j.config.Env;
import vip.lematech.hrun4j.config.NamespaceMap;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.core.converter.ObjectConverter;
import vip.lematech.hrun4j.core.loader.Searcher;
import vip.lematech.hrun4j.core.loader.TestDataLoaderFactory;
import vip.lematech.hrun4j.entity.testcase.Config;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import vip.lematech.hrun4j.entity.testcase.TestSuite;
import vip.lematech.hrun4j.entity.testcase.TestSuiteCase;
import vip.lematech.hrun4j.cli.handler.Command;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * The <code>run</code> command.
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class Run extends Command {

    @Option(name = "--testcase_path", usage = "list of testcase path to execute", metaVar = "<testcase_path>")
    List<String> testCasePaths;

    @Override
    public String description() {
        return "Print run command information.";
    }

    @Option(name = "--ext_name", usage = "Specify the use case extension.")
    String extName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;

    @Option(name = "--testsuite_path", usage = "Specify the TestSuite path", metaVar = "<testsuite_path>")
    String testSuitePathValue;

    @Option(name = "--dot_env_path", usage = "Specify the path to the.env file")
    File dotEnvPath;

    @Option(name = "--pkg_name", usage = "Specify the project package name.")
    String pkgName;

    @Option(name = "--bsh", usage = "Specify Hrun4j.bsh as the project path, not the current path.")
    File rootBsh;

    @Option(name = "--i18n", usage = "Internationalization support,support us/cn.")
    String i18n = Constant.I18N_CN;

    private Searcher searcher;
    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        RunnerConfig.getInstance().setRunMode(RunnerConfig.RunMode.CLI);
        LogHelper.info("Run mode: {}", RunnerConfig.RunMode.CLI);


        if (Objects.nonNull(testSuitePathValue) && Objects.nonNull(testCasePaths)) {
            String exceptionMsg = String.format("It is not allowed to specify both testSuitePath and testCasePaths option values");
            LogHelper.error(exceptionMsg);
            return 1;
        }

        if (Objects.isNull(testSuitePathValue) && Objects.isNull(testCasePaths)) {
            String exceptionMsg = String.format("You must specify either testSuitePath or testCasePaths");
            LogHelper.error(exceptionMsg);
            return 1;
        }



        validateOrSetParams();
        String workDirPath = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory());

        File testSuitePath = null  ;
        if(Objects.nonNull(testSuitePathValue)){
            testSuitePath = new File(workDirPath,testSuitePathValue);
        }
        LogHelper.info("The workspace path：{}", workDirPath);
        initEnvPath(workDirPath);
        TestSuite testSuite = new TestSuite();
        searcher = new Searcher();
        List<String> testCaseMapFiles = new ArrayList<>();
        if (CollUtil.isNotEmpty(testCasePaths)) {
            Config config = new Config();
            config.setName("Command line execution configuration name");
            testSuite.setConfig(config);
            List<TestSuiteCase> testSuiteCases = new ArrayList<>();
            HashSet<String> dataFilesSet = new HashSet();
            for (String dataFilePath : testCasePaths) {
                File dataFile = new File(workDirPath,dataFilePath);
                if (!dataFile.exists()) {
                    String exceptionMsg = String.format("The test case file %s does not exist"
                            , FileUtil.getAbsolutePath(dataFile));
                    LogHelper.error(exceptionMsg);
                    throw new DefinedException(exceptionMsg);
                }
                fileTraverse(dataFile, dataFilesSet);
            }
            for (String dataFilePath : dataFilesSet) {
                TestSuiteCase testSuiteCase = new TestSuiteCase();
                String dataResultFile = null;
                if (dataFilePath.startsWith(workDirPath)) {
                    workDirPath = LittleHelper.escapeRegexTransfer(workDirPath);
                    dataResultFile = dataFilePath.replaceFirst(workDirPath, Constant.DOT_PATH);
                }
                if (!StrUtil.isEmpty(dataResultFile)) {
                    testSuiteCase.setCaseRelativePath(dataResultFile);
                    testSuiteCases.add(testSuiteCase);
                }
            }
            testSuite.setTestCases(testSuiteCases);
        } else {

            String testSuiteExtName = FileUtil.extName(testSuitePath);
            testSuite = TestDataLoaderFactory.getLoader(testSuiteExtName)
                    .load(testSuitePath, TestSuite.class);
            if (Objects.isNull(testSuite)) {
                String exceptionMsg = String.format("TestSuite file %s is invalid, load data is empty, please check", FileUtil.getAbsolutePath(testSuitePath));
                LogHelper.error(exceptionMsg);
            }
        }
        Config testSuiteConfig = testSuite.getConfig();
        List<TestSuiteCase> testSuiteCases = testSuite.getTestCases();
        for (TestSuiteCase testSuiteCase : testSuiteCases) {
            String caseRelativePath = testSuiteCase.getCaseRelativePath();
            testCaseMapFiles.add(caseRelativePath);
            String extName = FileUtil.extName(caseRelativePath);
            String namespace = JavaIdentifierHelper.formatFilePath(caseRelativePath);
            if (!StrUtil.isEmpty(extName)) {
                namespace = JavaIdentifierHelper.formatFilePath(LittleHelper.replaceLast(caseRelativePath, Constant.DOT_PATH + extName, ""));
            }
            LogHelper.debug("namespace:{}", namespace);
            File dataFile = searcher.quicklySearchFile(caseRelativePath);
            TestCase testCase = TestDataLoaderFactory.getLoader(extName)
                    .load(dataFile, TestCase.class);
            Config testCaseConfig = testCase.getConfig();
            testCaseConfig.setParameters(null);
            Config resultConfig = (Config) ObjectConverter.objectsExtendsPropertyValue(testSuiteConfig, testCaseConfig);
            testCase.setConfig(resultConfig);
            Map environment = Env.getEnvMap();
            if (environment.containsKey(namespace)) {
                String exceptionMsg = String.format("If the same path case %s already exists, only the last one can be retained", caseRelativePath);
                LogHelper.warn(exceptionMsg);
            }
            NamespaceMap.setDataObject(String.format("%s:%s", RunnerConfig.RunMode.CLI, namespace), testCase);
        }
        if (testCaseMapFiles.size() == 0) {
            String exceptionMsg = String.format("The test case path cannot be empty");
            LogHelper.error(exceptionMsg);
            return 1;
        }
        RunnerConfig.getInstance().setTestCasePaths(testCaseMapFiles);
        TestNGEngine.run();
        return 0;
    }

    /**
     * file traverse
     *
     * @param file
     * @param dataFiles
     */
    private void fileTraverse(File file, HashSet<String> dataFiles) {
        if (file.isFile()) {
            String dataExtName = FileUtil.extName(file);
            if (extName.equalsIgnoreCase(dataExtName)) {
                dataFiles.add(FileUtil.getAbsolutePath(file));
            }
        } else {
            File[] fileList = file.listFiles();
            for (File subFile : fileList) {
                if (subFile.isFile()) {
                    String dataExtName = FileUtil.extName(subFile);
                    if (extName.equalsIgnoreCase(dataExtName)) {
                        dataFiles.add(FileUtil.getAbsolutePath(subFile));
                    }
                } else {
                    fileTraverse(subFile, dataFiles);
                }
            }
        }
    }

    /**
     * Initialize the path to the.env file
     *
     * @param workDirPath
     */
    private void initEnvPath(String workDirPath) {
        if (!Objects.isNull(dotEnvPath)) {
            if (!dotEnvPath.exists() || !dotEnvPath.isFile()) {
                String exceptionMsg = String.format("The .env file %s does not exist"
                        , FileUtil.getAbsolutePath(dotEnvPath));
                LogHelper.error(exceptionMsg);
                throw new DefinedException(exceptionMsg);
            }
            if (!FileUtil.isAbsolutePath(dotEnvPath.getPath())) {
                File dotFilePath = new File(workDirPath, Constant.ENV_FILE_NAME);
                RunnerConfig.getInstance().setDotEnvPath(FileUtil.getAbsolutePath(dotFilePath));
            } else {
                RunnerConfig.getInstance().setDotEnvPath(FileUtil.getAbsolutePath(dotEnvPath));
            }
        } else {
            File dotFilePath = new File(workDirPath);
            if (!dotFilePath.exists() || !dotFilePath.isFile()) {
                String exceptionMsg = String.format("The .env file %s does not exist"
                        , FileUtil.getAbsolutePath(dotFilePath));
                LogHelper.warn(exceptionMsg);
            }
            RunnerConfig.getInstance().setDotEnvPath(FileUtil.getAbsolutePath(dotFilePath));
        }
    }

    /**
     * Verify the validity of data or set parameter values
     */
    private void validateOrSetParams() {

        if (Objects.isNull(rootBsh)) {
            RunnerConfig.getInstance().setWorkDirectory(new File(Constant.DOT_PATH));
        } else {
            if (!rootBsh.exists() || !rootBsh.isFile() || !Constant.BEANSHELL_BSH_END_SUFFIX.endsWith(FileUtil.extName(rootBsh))) {
                String exceptionMsg = String.format("The rootBsh file %s does not exist or the suffix does not end in `.bsh`"
                        , FileUtil.getAbsolutePath(rootBsh));
                throw new DefinedException(exceptionMsg);
            }
            File workFile = rootBsh.getParentFile();
            String workDirPath = FileUtil.getAbsolutePath(workFile);
            Properties property = System.getProperties();
            property.setProperty("user.dir", workDirPath);
            RunnerConfig.getInstance().setWorkDirectory(new File(workDirPath));
        }
        if (!StrUtil.isEmpty(pkgName)) {
            RunnerConfig.getInstance().setPkgName(pkgName);
        }
        if (!StrUtil.isEmpty(extName)) {
            RunnerConfig.getInstance().setTestCaseExtName(extName);
        }
        if (!StrUtil.isEmpty(i18n)) {
            RunnerConfig.getInstance().setI18n(i18n);
        }

    }

}
