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
    private static TestNG testNG;
    private static String suiteName;
    public static Map<String, Set<String>> testCasePkgGroup = new HashMap<>();

    public static TestNG getInstance() {
        if (testNG == null) {
            testNG = new TestNG();
            setDefaultProperties();
        }
        return testNG;
    }

    /**
     * Setting Default Properties
     */
    private static void setDefaultProperties() {
        testNG.setDefaultSuiteName("hrun4j");
        HTMLReporter htmlReporter = new HTMLReporter();
        JUnitXMLReporter jUnitXMLReporter = new JUnitXMLReporter();
        testNG.addListener(htmlReporter);
        testNG.addListener(jUnitXMLReporter);
    }

    /**
     * self-defined add listener
     * @param testListenerList testListenerList
     * @return TestNG
     */
    public static TestNG addListener(List<ITestListener> testListenerList) {
        getInstance();
        if (testListenerList.size() > 0) {
            for (ITestListener testListener : testListenerList) {
                testNG.addListener(testListener);
            }
        }
        return testNG;
    }

    /**
     * init testng classes and testng  run
     */
    public static void run() {
        List<String> testCasePaths = RunnerConfig.getInstance().getTestCasePaths();
        testCasePkgGroup = fileList2TestClass(testCasePaths);
        if (MapUtil.isEmpty(testCasePkgGroup)) {
            LogHelper.warn("No valid test cases were found on the current path: {}", testCasePaths);
        }
        addTestClasses();
        runNG();
    }

    private static void runNG() {
        getInstance().run();
    }

    /**
     * Dynamically construct test cases
     */
    private static void addTestClasses() {
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
        getInstance().setTestClasses(execClass);
    }


    /**
     * file path flag , default true is relative path, otherwise abosolute path
     */
    private static boolean filePathFlag = true;

    private static void fileToTestClassMap(Map<String, Set<String>> fileTestClassMap, File file) {
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
            pkgName.append(RunnerConfig.getInstance().getPkgName());
            String filePath;
            if (filePathFlag) {
                pkgName.append("_");
                String workDirPath = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory());
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
    private static void fileTraverse(File files, Map<String, Set<String>> fileTestClassMap) {
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
    public static Map<String, Set<String>> fileList2TestClass(List<String> listFile) {
        Map<String, Set<String>> fileTestClassMap = Maps.newHashMap();
        for (String fileStr : listFile) {
            File file = new File(fileStr);
            if (FileUtil.isAbsolutePath(file.getPath())) {
                filePathFlag = false;
            }
            if (filePathFlag) {
                file = new File(RunnerConfig.getInstance().getWorkDirectory().getPath(), file.getPath());
            }
            FilesHelper.checkFileExists(file);
            fileTraverse(file, fileTestClassMap);
        }

        return fileTestClassMap;
    }

}
