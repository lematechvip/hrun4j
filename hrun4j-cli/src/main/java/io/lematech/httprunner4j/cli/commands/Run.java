package io.lematech.httprunner4j.cli.commands;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.cli.testsuite.TestNGEngine;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.Env;
import io.lematech.httprunner4j.config.NamespaceMap;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.converter.ObjectConverter;
import io.lematech.httprunner4j.core.loader.Searcher;
import io.lematech.httprunner4j.core.loader.TestDataLoaderFactory;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestSuite;
import io.lematech.httprunner4j.entity.testcase.TestSuiteCase;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import io.lematech.httprunner4j.widget.utils.SmallUtil;
import org.kohsuke.args4j.Option;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className The <code>run</code> command.
 * @description TODO
 * @created 2021/4/18 7:49 下午
 * @publicWechat lematech
 */
public class Run extends Command {

    @Option(name = "--testcase_paths", usage = "list of testcase path to execute", metaVar = "<testcase_paths>")
    List<String> testCasePaths;

    @Override
    public String description() {
        return "Print run command information.";
    }

    @Option(name = "--ext_name", usage = "Specify the use case extension.")
    String extName = Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME;

    @Option(name = "--testsuite_path", usage = "Specify the TestSuite path", metaVar = "<testsuite_path>")
    File testSuitePath;

    @Option(name = "--dot_env_path", usage = "Specify the path to the.env file")
    File dotEnvPath;

    @Option(name = "--pkg_name", usage = "Specify the project package name.")
    String pkgName;

    @Option(name = "--testjar", usage = "Specifies a jar file that contains aviator exp implemenets.")
    File testJar;

    @Option(name = "--i18n", usage = "Internationalization support,support us/cn.")
    String i18n = Constant.I18N_CN;

    private Searcher searcher;
    private ObjectConverter objectConverter = new ObjectConverter();
    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        RunnerConfig.getInstance().setRunMode(RunnerConfig.RunMode.CLI);
        MyLog.info("Run mode: {}", RunnerConfig.RunMode.CLI);
        if (Objects.nonNull(testSuitePath) && Objects.nonNull(testCasePaths)) {
            String exceptionMsg = String.format("It is not allowed to specify both testSuitePath and testCasePaths option values");
            MyLog.error(exceptionMsg);
            return 1;
        }

        if (Objects.isNull(testSuitePath) && Objects.isNull(testCasePaths)) {
            String exceptionMsg = String.format("You must specify either testSuitePath or testCasePaths");
            MyLog.error(exceptionMsg);
            return 1;
        }

        validateOrSetParams();
        String workDirPath = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory());
        MyLog.info("The workspace path：{}", workDirPath);
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
                File dataFile = new File(dataFilePath);
                if (!dataFile.exists()) {
                    String exceptionMsg = String.format("The test case file %s does not exist"
                            , FileUtil.getAbsolutePath(dataFile));
                    MyLog.error(exceptionMsg);
                    throw new DefinedException(exceptionMsg);
                }
                fileTraverse(dataFile, dataFilesSet);
            }
            for (String dataFilePath : dataFilesSet) {
                TestSuiteCase testSuiteCase = new TestSuiteCase();
                String dataResultFile = null;
                if (dataFilePath.startsWith(workDirPath)) {
                    dataResultFile = dataFilePath.replaceFirst(workDirPath, Constant.DOT_PATH);
                }
                if (!StrUtil.isEmpty(dataResultFile)) {
                    testSuiteCase.setCaseRelativePath(dataResultFile);
                    testSuiteCases.add(testSuiteCase);
                }
            }
            testSuite.setTestCases(testSuiteCases);
        } else {
            FilesUtil.checkFileExists(testSuitePath);
            String testSuiteExtName = FileUtil.extName(testSuitePath);
            testSuite = TestDataLoaderFactory.getLoader(testSuiteExtName)
                    .load(testSuitePath, TestSuite.class);
            if (Objects.isNull(testSuite)) {
                String exceptionMsg = String.format("TestSuite file %s is invalid, load data is empty, please check", FileUtil.getAbsolutePath(testSuitePath));
                MyLog.error(exceptionMsg);
            }
        }
        Config testSuiteConfig = testSuite.getConfig();
        List<TestSuiteCase> testSuiteCases = testSuite.getTestCases();
        for (TestSuiteCase testSuiteCase : testSuiteCases) {
            String caseRelativePath = testSuiteCase.getCaseRelativePath();
            testCaseMapFiles.add(caseRelativePath);
            String extName = FileUtil.extName(caseRelativePath);
            String namespace = JavaIdentifierUtil.formatFilePath(caseRelativePath);
            if (!StrUtil.isEmpty(extName)) {
                namespace = JavaIdentifierUtil.formatFilePath(SmallUtil.replaceLast(caseRelativePath, Constant.DOT_PATH + extName, ""));
            }
            MyLog.info("namespace:{}", namespace);
            File dataFile = searcher.quicklySearchFile(caseRelativePath);
            TestCase testCase = TestDataLoaderFactory.getLoader(extName)
                    .load(dataFile, TestCase.class);
            Config testCaseConfig = testCase.getConfig();
            testCaseConfig.setParameters(null);
            Config resultConfig = (Config) objectConverter.objectsExtendsPropertyValue(testSuiteConfig, testCaseConfig);
            testCase.setConfig(resultConfig);
            Map environment = Env.getEnvMap();
            if (environment.containsKey(namespace)) {
                String exceptionMsg = String.format("If the same path case %s already exists, only the last one can be retained", caseRelativePath);
                MyLog.warn(exceptionMsg);
            }
            NamespaceMap.setDataObject(String.format("%s:%s", RunnerConfig.RunMode.CLI, namespace), testCase);
        }
        if (testCaseMapFiles.size() == 0) {
            String exceptionMsg = String.format("The test case path cannot be empty");
            MyLog.error(exceptionMsg);
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
                MyLog.error(exceptionMsg);
                throw new DefinedException(exceptionMsg);
            }
            if (!FileUtil.isAbsolutePath(dotEnvPath.getPath())) {
                File dotFilePath = new File(workDirPath, Constant.ENV_FILE_NAME);
                RunnerConfig.getInstance().setDotEnvPath(FileUtil.getAbsolutePath(dotFilePath));
            } else {
                RunnerConfig.getInstance().setDotEnvPath(FileUtil.getAbsolutePath(dotEnvPath));
            }
        } else {
            File dotFilePath = new File(workDirPath, Constant.ENV_FILE_NAME);
            if (!dotFilePath.exists() || !dotFilePath.isFile()) {
                String exceptionMsg = String.format("The .env file %s does not exist"
                        , FileUtil.getAbsolutePath(dotFilePath));
                MyLog.warn(exceptionMsg);
            }
            RunnerConfig.getInstance().setDotEnvPath(FileUtil.getAbsolutePath(dotFilePath));
        }
    }

    /**
     * Verify the validity of data or set parameter values
     */
    private void validateOrSetParams() {

        if (Objects.isNull(testJar)) {
            RunnerConfig.getInstance().setWorkDirectory(new File(Constant.DOT_PATH));
        } else {
            if (!testJar.exists() || !testJar.isFile() || !Constant.TEST_JAR_END_SUFFIX.endsWith(FileUtil.extName(testJar))) {
                String exceptionMsg = String.format("The TestJar file %s does not exist or the suffix does not end in `.jar`"
                        , FileUtil.getAbsolutePath(testJar));
                throw new DefinedException(exceptionMsg);
            }
            File workFile = testJar.getParentFile();
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
