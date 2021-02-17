package io.lematech.httprunner4j.testng;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.itranswarp.compiler.JavaStringCompiler;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.handler.Executor;
import io.lematech.httprunner4j.utils.TimeString;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RunTestCase extends TestBase {
    private Executor executor = new Executor();
    @Test(dataProvider = "dataProvider")
    public void hrun4j_demo_testcase(TestCase testCase){
        executor.execute(testCase);
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
*/        Map<String, List<String>> pkgGroup =  Maps.newHashMap();
        RunTestCase.generatePkgGroup(new File(TEST_CASE_FILE_PATH),pkgGroup);
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        try {
            velocityEngine.init();
            // 获取模板文件
            Template t = velocityEngine.getTemplate(TEST_TEMPLATE_FILE_PATH);
            // 设置变量
            VelocityContext ctx = new VelocityContext();
            log.info("map：{}", JSON.toJSONString(pkgGroup));

            TestNG testNG = new TestNG();
            TestListenerAdapter listener = new TestListenerAdapter();
            testNG.addListener(listener);
            /**添加需要执行的测试类数组*/
            //testNG.setTestClasses(new Class[]{TestNgSuiteTest.class});

            List<Class> classes = new ArrayList<>();
            for(Map.Entry<String,List<String>> entry:pkgGroup.entrySet()){
                String fullTestClassName = entry.getKey();
                List methodNameList = entry.getValue();
                String pkgName = StrUtil.subBefore(fullTestClassName,".",true);
                String className = StrUtil.upperFirst(StrUtil.subAfter(fullTestClassName,".",true));
                log.info("完整包名：{},包名：{},类名：{}",fullTestClassName,pkgName,className);
                ctx.put("pkgName", pkgName);
                ctx.put("className", className);
                ctx.put("methodList", methodNameList);
                StringWriter sw = new StringWriter();
                t.merge(ctx,sw);
                JavaStringCompiler compiler = new JavaStringCompiler();
                Map<String, byte[]> results = compiler.compile(String.format("%s.java",className), sw.toString());
                log.info("源代码内容：{}", sw.toString());
                String pkgClassName = String.format("%s.%s",pkgName,className);
                log.info("类名结构：{}",pkgClassName);
                Class<?> clazz = compiler.loadClass(pkgClassName, results);
                log.info("类名：{}",clazz.getName());
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
            long maxtime = interval.stream().max(Comparator.naturalOrder()).get();

            /**最小耗时*/
            long mintime = interval.stream().min(Comparator.naturalOrder()).get();

            /**平均耗时*/
            double avgtime = interval.stream().mapToDouble(i -> i).average().getAsDouble();

            System.out.println(String.format("test result: [success: %s],[failure: %s]",pass.size(),failed.size()));

            System.out.println(String.format("performance analysis: [maxtime: %s(ms)],[mintime: %s(ms),[avgtime: %s(ms)]]",
                    maxtime, mintime, avgtime));
            //
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static final String TEST_TEMPLATE_FILE_PATH = "testClass.vm";
    private static final String TEST_CASE_FILE_PATH = "src/test/resources/testcases";

    private static void generatePkgGroup(File listFile,Map<String, List<String>> pkgGroup){
        if(!listFile.exists()){
            log.error("文件{}不存在",listFile.getName());
        }
        File [] files = listFile.listFiles();
        for(File file : files){
            if(file.isFile()){
                String pkgPath = file.getParent().replace(TEST_CASE_FILE_PATH,"");
                String pkgRootKey = String.format("hrun4j%s", TimeString.getTimeString());
                String []pkgNames = pkgPath2pkgName(pkgPath);
                StringBuffer pkgName = new StringBuffer();
                pkgName.append(pkgRootKey);
                for(String pkgPortionName:pkgNames){
                    if(StrUtil.isEmpty(pkgPortionName)){
                        continue;
                    }
                    pkgName.append(".");
                    pkgName.append(pkgPortionName);
                }
                String fullPkgName = pkgName.toString();
                //log.info("完整包路径地址：{},文件名及后缀：{}",fullPkgName,file.getName());
                String fileMainName = FileNameUtil.mainName(file.getName());
                String testClassName = String.format("%sTest",file.getParentFile().getName());
                String fullTestClassName = fileMainName+"."+testClassName;
                //log.info("完整包路径地址：{},类文件名：{},完整包名类名组合：{},方法名：{}",fullPkgName,testClassName,fullTestClassName,fileMainName);
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
    private static String[] pkgPath2pkgName(String pkgPath){
        if(StrUtil.isEmpty(pkgPath)){
           // log.error("文件目录为空或不存在");
            return new String[]{};
        }
        String[]pkgName = pkgPath.split("/");
        return pkgName;
    }

}
