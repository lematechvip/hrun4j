package vip.lematech.hrun4j.cli.testsuite;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.helper.LittleHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.core.engine.TemplateEngine;
import vip.lematech.hrun4j.core.loader.HotLoader;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.config.RunnerConfig;
import lombok.Data;
import org.apache.velocity.VelocityContext;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.uncommons.reportng.HTMLReporter;
import java.io.File;
import java.util.*;


/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

@Data
public class TestNGEngine {
    private TestNG testNG;
    private String suiteName;
    private RunnerConfig runnerConfig;
    public Map<String, Set<String>> testCasePkgGroup = new HashMap<>();

    public TestNGEngine(RunnerConfig runnerConfig) {
        this.runnerConfig = runnerConfig;
        this.testNG = new TestNG();
        setDefaultProperties();
    }

    /**
     * Setting Default Properties
     */
    private void setDefaultProperties() {
        testNG.setDefaultSuiteName(RunnerConfig.DEFAULT_TEST_SUITE_NAME);
        HTMLReporter htmlReporter = new HTMLReporter();
        JUnitXMLReporter jUnitXMLReporter = new JUnitXMLReporter();
        testNG.addListener(htmlReporter);
        testNG.addListener(jUnitXMLReporter);
    }

    public void setSuiteName(String name) {
        testNG.setDefaultSuiteName(name);
    }

    /**
     * self-defined add listener
     * @param testListenerList testListenerList
     * @return TestNG
     */
    public TestNGEngine addListener(List<ITestListener> testListenerList) {
        if (testListenerList.size() > 0) {
            for (ITestListener testListener : testListenerList) {
                testNG.addListener(testListener);
            }
        }
        return this;
    }

    /**
     * init testng classes and testng  run
     */
    public void run() {
        List<String> testCasePaths = runnerConfig.getTestCasePaths();
        testCasePkgGroup = fileList2TestClass(testCasePaths);
        if (MapUtil.isEmpty(testCasePkgGroup)) {
            LogHelper.warn("No valid test cases were found on the current path: {}", testCasePaths);
        }
        addTestClasses();
        runNG();
    }

    private void runNG() {
        testNG.run();
    }

    /**
     * Dynamically construct test cases
     */
    private void addTestClasses() {
        List<Class> classes = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : testCasePkgGroup.entrySet()) {
            String fullTestClassName = entry.getKey();
            Set methodNameList = entry.getValue();
            String pkgName = StrUtil.subBefore(fullTestClassName, Constant.DOT_PATH, true);
            String className = StrUtil.upperFirst(StrUtil.subAfter(fullTestClassName, Constant.DOT_PATH, true));
            VelocityContext ctx = new VelocityContext();
            ctx.put("pkgName", pkgName);
            ctx.put("className", className);
            ctx.put("methodList", methodNameList);
            String templateRenderContent = TemplateEngine.getTemplateRenderContent(Constant.TEST_TEMPLATE_FILE_PATH, ctx);
            LogHelper.debug("Render Template Engine Content:{}", templateRenderContent);
            Class<?> clazz = HotLoader.hotLoadClass(pkgName, className, templateRenderContent);
            classes.add(clazz);
            LogHelper.debug("Class full path：'{}',package path：'{}',class name：{} added done.", fullTestClassName, pkgName, className);
        }
        Class[] execClass = classes.toArray(new Class[0]);
        testNG.setTestClasses(execClass);
    }


    /**
     * file path flag , default true is relative path, otherwise abosolute path
     */
    private static boolean filePathFlag = true;

    private void fileToTestClassMap(Map<String, Set<String>> fileTestClassMap, File file) {
        String extName = FileUtil.extName(file);
        if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)
                || Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
            String fileMainName = FileNameUtil.mainName(file.getName());
            String fileCanonicalPath = FileUtil.getAbsolutePath(file);
            if (!JavaIdentifierHelper.isValidJavaIdentifier(fileMainName)) {
                String exceptionMsg = String.format("File name:%s  does not match Java identifier(No special characters are allowed. The first character must be '$',' _ ', 'letter'. No special characters such as' - ', 'space', '/' are allowed), in the path: ", fileCanonicalPath);
                throw new DefinedException(exceptionMsg);
            }
            String fileParentCanonicalPath = FileUtil.getAbsolutePath(file.getParentFile());
            StringBuffer pkgName = new StringBuffer();
            pkgName.append(runnerConfig.getPkgName());
            String filePath;
            if (filePathFlag) {
                pkgName.append("_");
                String workDirPath = FileUtil.getAbsolutePath(runnerConfig.getWorkDirectory());
                workDirPath =  LittleHelper.escapeRegexTransfer(workDirPath);
                filePath = fileParentCanonicalPath.replaceFirst(workDirPath, "");
            } else {
                filePath = fileParentCanonicalPath;
            }
            String transferPackageName = FilesHelper.dirPath2pkgName(filePath);
            String validatePackageInfo = JavaIdentifierHelper.validateIdentifierName(transferPackageName);
            if (!StrUtil.isEmpty(validatePackageInfo)) {
                throw new DefinedException(validatePackageInfo);
            }
            pkgName.append(Constant.DOT_PATH).append(transferPackageName);
            StringBuffer pkgTestClassMetaInfo = new StringBuffer(pkgName.toString());
            String folderName = file.getParentFile().getName();
            String testClassName = StrUtil.upperFirst(StrUtil.toCamelCase(String.format("%sTest", folderName)));
            pkgTestClassMetaInfo.append(Constant.DOT_PATH).append(testClassName);
            String fullTestClassName = pkgTestClassMetaInfo.toString();
            LogHelper.debug("Complete class package name:{}, filename: {},method name: {}", fullTestClassName, testClassName, fileMainName);
            if (fileTestClassMap.containsKey(fullTestClassName)) {
                Set<String> testClassList = fileTestClassMap.get(fullTestClassName);
                testClassList.add(fileMainName);
            } else {
                Set<String> testClassList = new HashSet<>();
                testClassList.add(fileMainName);
                fileTestClassMap.put(fullTestClassName, testClassList);
            }
        } else {
            LogHelper.debug("Current file {}.{} format support, only support YML or JSON file suffix", file.getPath(), file.getName());
        }
    }

    /**
     * file traverse
     *
     * @param files
     * @param fileTestClassMap
     */
    private void fileTraverse(File files, Map<String, Set<String>> fileTestClassMap) {
        if (files.isFile()) {
            fileToTestClassMap(fileTestClassMap, files);
        } else {
            File[] fileList = files.listFiles();
            for (File file : fileList) {
                if (file.isFile()) {
                    fileToTestClassMap(fileTestClassMap, file);
                } else {
                    fileTraverse(file, fileTestClassMap);
                }
            }
        }
    }


    /**
     * @param listFile directories file list
     * @return file to map
     */
    public Map<String, Set<String>> fileList2TestClass(List<String> listFile) {
        Map<String, Set<String>> fileTestClassMap = Maps.newHashMap();
        for (String fileStr : listFile) {
            File file = new File(fileStr);
            if (FileUtil.isAbsolutePath(file.getPath())) {
                filePathFlag = false;
            }
            if (filePathFlag) {
                file = new File(runnerConfig.getWorkDirectory().getPath(), file.getPath());
            }
            FilesHelper.checkFileExists(file);
            fileTraverse(file, fileTestClassMap);
        }

        return fileTestClassMap;
    }

}
