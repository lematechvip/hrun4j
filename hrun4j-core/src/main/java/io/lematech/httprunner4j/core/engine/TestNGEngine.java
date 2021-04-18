package io.lematech.httprunner4j.core.engine;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.loader.HotLoader;
import io.lematech.httprunner4j.core.loader.TestDataLoaderFactory;
import io.lematech.httprunner4j.core.validator.SchemaValidator;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import io.lematech.httprunner4j.widget.utils.RegularUtil;
import io.lematech.httprunner4j.widget.log.MyLog;
import lombok.Data;
import org.apache.velocity.VelocityContext;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.uncommons.reportng.HTMLReporter;

import java.io.File;
import java.util.*;

@Data
public class TestNGEngine {
    private static TestNG testNG;
    private static String suiteName;
    private static SchemaValidator schemaValidator = new SchemaValidator();
    private static Map<String, List<String>> testCasePkgGroup = new HashMap<>();
    public static TestNG getInstance(){
        if(testNG == null){
            testNG = new TestNG();
            setDefaultProperties();
        }
        return testNG;
    }
    private static void setDefaultProperties(){
        testNG.setDefaultSuiteName("httprunner4j");
        HTMLReporter htmlReporter = new HTMLReporter();
        JUnitXMLReporter jUnitXMLReporter = new JUnitXMLReporter();
        testNG.addListener(htmlReporter);
        testNG.addListener(jUnitXMLReporter);
    }

    /**
     * self-defined add listener
     * @param testListenerList
     * @return
     */
    public static TestNG addListener(List<ITestListener> testListenerList){
        getInstance();
        if(testListenerList.size()>0){
            for(ITestListener testListener : testListenerList){
                testNG.addListener(testListener);
            }
        }
        return testNG;
    }
    public static void run(){
        List<File> executePaths = RunnerConfig.getInstance().getExecutePaths();
        for (File execPath : executePaths) {
            MyLog.info("execute path : [{}] test cases", execPath);
            traversePkgTestCaseGroup(execPath);
        }
        if (MapUtil.isEmpty(testCasePkgGroup)) {
            MyLog.warn("in path [{}] not found valid testcases", executePaths);
        }
        addTestClasses();
        runNG();
    }
    private static void runNG(){
        getInstance().run();
    }

    private static void addTestClasses(){
        List<Class> classes = new ArrayList<>();
        for(Map.Entry<String,List<String>> entry:testCasePkgGroup.entrySet()){
            String fullTestClassName = entry.getKey();
            List methodNameList = entry.getValue();
            String pkgName = StrUtil.subBefore(fullTestClassName,".",true);
            String className = StrUtil.upperFirst(StrUtil.subAfter(fullTestClassName,".",true));
            VelocityContext ctx = new VelocityContext();
            ctx.put("pkgName", pkgName);
            ctx.put("className", className);
            ctx.put("methodList", methodNameList);
            String templateRenderContent = TemplateEngine.getTemplateRenderContent(Constant.TEST_TEMPLATE_FILE_PATH,ctx);
            MyLog.debug("test case content:{}", templateRenderContent);
            Class<?> clazz = HotLoader.hotLoadClass(pkgName,className,templateRenderContent);
            classes.add(clazz);
            MyLog.debug("class full path：[{}],pkg path：[{}],class name：{} added done.", fullTestClassName, pkgName, className);
        }
        Class [] execClass = classes.toArray(new Class[0]);
        getInstance().setTestClasses(execClass);
    }

    private static void traversePkgTestCaseGroup(File listFile){
        if(!listFile.exists()){
            String exceptionMsg = String.format("file: %s is not exist", listFile.getAbsolutePath());
            throw new DefinedException(exceptionMsg);
        }
        File [] files = listFile.listFiles();
        for(File file : files){
            if(file.isFile()){
                String extName = FileUtil.extName(file);
                String fileMainName = JavaIdentifierUtil.toValidJavaIdentifier(FileNameUtil.mainName(file.getName()), 0);
                if(Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)
                        ||Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)){
                    String pkgPath = file.getParent().replace(Constant.TEST_CASE_FILE_PATH,"");

                    if(!JavaIdentifierUtil.isValidJavaIdentifier(fileMainName)){
                        String exceptionMsg = String.format("%s.%s,file name is invalid,not apply java identifier,please modify it", pkgPath, fileMainName);
                        throw new DefinedException(exceptionMsg);
                    }
                    TestDataLoaderFactory.getLoader(extName).load(file, TestCase.class);
                    StringBuffer pkgName = new StringBuffer();
                    String selfPkgName = RunnerConfig.getInstance().getPkgName();
                    pkgName.append(StrUtil.isEmpty(selfPkgName) ? Constant.SELF_ROOT_PKG_NAME : selfPkgName);
                    pkgName.append(RegularUtil.dirPath2pkgName(pkgPath));
                    StringBuffer classPkgPath = new StringBuffer(JavaIdentifierUtil.toValidJavaIdentifier(pkgName.toString(), 1));
                    String folderName = file.getParentFile().getName();
                    String testClassName = StrUtil.upperFirst(StrUtil.toCamelCase(String.format("%sTest", JavaIdentifierUtil.toValidJavaIdentifier(folderName, 0))));
                    classPkgPath.append(Constant.DOT_PATH).append(testClassName);
                    String fullTestClassName = classPkgPath.toString();
                    MyLog.debug("full test class name is：{},class file is：{},method name is：{}", fullTestClassName, testClassName, fileMainName);
                    if (testCasePkgGroup.containsKey(fullTestClassName)) {
                        List<String> testClassList = testCasePkgGroup.get(fullTestClassName);
                        testClassList.add(fileMainName);
                    } else {
                        List<String> testClassList = new ArrayList<>();
                        testClassList.add(fileMainName);
                        testCasePkgGroup.put(fullTestClassName, testClassList);
                    }
                }else {
                    MyLog.debug("in pkgPath {} file {} not support,only support .json or.yml suffix", file.getPath(), fileMainName);
                }
            }else{
                traversePkgTestCaseGroup(file);
            }
        }
    }


}
