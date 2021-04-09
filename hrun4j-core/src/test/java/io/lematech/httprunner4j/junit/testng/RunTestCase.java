package io.lematech.httprunner4j.junit.testng;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.core.loader.HotLoader;
import io.lematech.httprunner4j.core.engine.TemplateEngine;
import io.lematech.httprunner4j.core.engine.TestCaseExecutorEngine;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.utils.log.MyLog;
import org.apache.velocity.VelocityContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.collections.Maps;
import org.testng.reporters.JUnitXMLReporter;
import org.uncommons.reportng.HTMLReporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RunTestCase extends TestBase {
    @Test(dataProvider = "dataProvider")
    public void hrun4j_demo_testcase(TestCase testCase){
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }
    @Test
    public void testDefinedExecEngine(){
        /**
         * 操作步骤：
         * 1、加载testcase数据文件，根据目录结构进行分类分组
         * 2、根据模板及分类策略，动态创建@testclass及@test方法
         * 3、通过自定义类加载器动态加载创建的class
         * 4、把动态加载的class，注入到testng对象中
         * 5、根据class配置的参数和testng配置的参数同步testng配置中
         *
         * 注意事项：
         * 1、多线程运行，同包同类文件创建，需要区分，可在包命名时动态生成唯一文件夹（顶层目录）
         * 2、运行之后的卸载问题，以防out of memeory
*/      Map<String, List<String>> pkgGroup =  Maps.newHashMap();
        RunTestCase.generatePkgGroup(new File(Constant.TEST_CASE_FILE_PATH),pkgGroup);
        try {
            // 设置变量

            MyLog.info("map：{}", JSON.toJSONString(pkgGroup));
            TestNG testNG = new TestNG();
            TestListenerAdapter listener = new TestListenerAdapter();
            HTMLReporter htmlReporter = new HTMLReporter();
            JUnitXMLReporter jUnitXMLReporter = new JUnitXMLReporter();
            testNG.addListener(htmlReporter);
            testNG.addListener(jUnitXMLReporter);
            testNG.setDefaultSuiteName("测试用例集");
            /**添加需要执行的测试类数组*/
            //testNG.setTestClasses(new Class[]{TestNgSuiteTest.class});

            List<Class> classes = new ArrayList<>();
            for(Map.Entry<String,List<String>> entry:pkgGroup.entrySet()){
                String fullTestClassName = entry.getKey();
                List methodNameList = entry.getValue();
                String pkgName = StrUtil.subBefore(fullTestClassName,".",true);
                String className = StrUtil.upperFirst(StrUtil.subAfter(fullTestClassName,".",true));
                MyLog.info("完整包名：{},包名：{},类名：{}", fullTestClassName, pkgName, className);
                VelocityContext ctx = new VelocityContext();
                ctx.put("pkgName", pkgName);
                ctx.put("className", className);
                ctx.put("methodList", methodNameList);
                String templateRenderContent = TemplateEngine.getTemplateRenderContent(Constant.TEST_TEMPLATE_FILE_PATH,ctx);
                Class<?> clazz = HotLoader.hotLoadClass(pkgName,className,templateRenderContent);
                classes.add(clazz);
            }
            Class [] execClass = classes.toArray(new Class[0]);
            testNG.setTestClasses(execClass);
            // 输出
            /**执行测试用例*/
            testNG.run();
            /**汇总分析*/
            List<ITestResult> pass = listener.getPassedTests();
            List<ITestResult> failed = listener.getFailedTests();
            List<Long> interval = pass.stream().map(x -> (x.getEndMillis() - x.getStartMillis())).collect(Collectors.toList());
            /**最大耗时*/
            //long maxtime = interval.stream().max(Comparator.naturalOrder()).get();

            /**最小耗时*/
           // long mintime = interval.stream().min(Comparator.naturalOrder()).get();

            /**平均耗时*/
           // double avgtime = interval.stream().mapToDouble(i -> i).average().getAsDouble();

           // System.out.println(String.format("test result: [success: %s],[failure: %s]",pass.size(),failed.size()));

           // System.out.println(String.format("performance analysis: [maxtime: %s(ms)],[mintime: %s(ms),[avgtime: %s(ms)]]",maxtime, mintime, avgtime));
            //
            //testNG.setOutputDirectory("./myreportng");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void generatePkgGroup(File listFile,Map<String, List<String>> pkgGroup){
        if(!listFile.exists()){
            MyLog.error("file: {} is not exist", listFile.getName());
        }
        File [] files = listFile.listFiles();
        for(File file : files){
            if(file.isFile()){
                String pkgPath = file.getParent().replace(Constant.TEST_CASE_FILE_PATH,"");
                //String pkgRootKey = String.format("hrun4j%s", TimeString.getTimeString());
                StringBuffer pkgName = new StringBuffer(dirPath2pkgName(pkgPath));
                String fileMainName = FileNameUtil.mainName(file.getName());
                String folderName = file.getParentFile().getName();
                String testClassName = StrUtil.upperFirst(StrUtil.toCamelCase(String.format("%sTest",folderName)));
                pkgName.append(".").append(testClassName);
                String fullTestClassName = pkgName.toString();
                MyLog.debug("完整包路径地址：{},类文件名：{},方法名：{}", fullTestClassName, testClassName, fileMainName);
                if(pkgGroup.containsKey(fullTestClassName)){
                    List testClassList = pkgGroup.get(fullTestClassName);
                    //todo:文件名称不能以数值开头
                    testClassList.add(fileMainName);
                }else{
                    List testClassList = new ArrayList<>();
                    testClassList.add(fileMainName);
                    pkgGroup.put(fullTestClassName,testClassList);
                }
            }else{
                generatePkgGroup(file,pkgGroup);
            }
        }
    }
    private static String dirPath2pkgName(String pkgPath){
        StringBuffer pkgName = new StringBuffer();
        pkgName.append(Constant.SELF_ROOT_PKG_NAME);
        if(StrUtil.isEmpty(pkgPath)){
            return pkgName.toString();
        }
        pkgName.append(pkgPath.replaceAll("/",Constant.DOT_PATH));
        return pkgName.toString();
    }

}
