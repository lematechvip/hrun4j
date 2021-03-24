package io.lematech.httprunner4j.junit.testng;

import com.itranswarp.compiler.JavaStringCompiler;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestNgTest {

    @Test
    public void dyGenerateTestClass(){
        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        Template template = null;
        try {
            velocityEngine.init();
            template = velocityEngine.getTemplate("testClass.vm");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 准备点数据,可能从配置文件读取
        List<String> fields = Arrays.asList("name", "age", "title", "content");

        final VelocityContext context = new VelocityContext();
        context.put("fields", fields);
        // 或者写入文件
        try {
            final StringWriter writer = new StringWriter();
            template.merge(context, writer);
            System.out.println(writer.toString());
            File file = new File("./src/test/java/io/lematech/httprunner4j/testng/TestNgSuiteTest.java");
            System.out.println(file.getAbsolutePath());
            Writer fileWriter = new PrintWriter(file);
            template.merge(context, fileWriter);
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJavaCompiler(){
        try {
            String className = "io.lematech.httprunner4j.junit.testng.TestNgSuiteTest";
            //TestNgSuiteTest testNgSuiteTest = new TestNgSuiteTest();
            //System.out.println("before:" + testNgSuiteTest);
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(className);
            CtMethod mthd = CtNewMethod.make("public String test() { return \"test() is called \"+ toString();  }", cc);
            cc.addMethod(mthd);
          //  AppClassLoader appClassLoader = AppClassLoader.getInstance();
           // Class<?> clazz = appClassLoader.findClassByBytes(className, cc.toBytecode());
//            clazz.getDeclaredConstructor().newInstance();
          //  Object obj = appClassLoader.getObj(clazz,testNgSuiteTest);
        //    System.out.println("after:" + obj);
            //测试反射调用添加的方法
          //  System.out.println(obj.getClass().getDeclaredMethod("test").invoke(obj));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testNgSuite(){
        JavaStringCompiler compiler = new JavaStringCompiler();
        Class<?> clazz ;
        try {

            Map<String, byte[]> results = compiler.compile("TestNgSuiteTest.java", JAVA_SOURCE_CODE_TESTNG);
            clazz = compiler.loadClass("io.lematech.httprunner4j.junit.testng.TestNgSuiteTest", results);
            //clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**初始化testNG对象*/
        TestNG testNG = new TestNG();
        TestListenerAdapter listener = new TestListenerAdapter();
        testNG.addListener(listener);
        /**添加需要执行的测试类数组*/
       // testNG.setTestClasses(new Class[]{TestNgSuiteTest.class});
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
    }

    @Test
    public void dyGenerateClass() throws IOException, ClassNotFoundException {
        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results = compiler.compile("UserProxy.java", JAVA_SOURCE_CODE);
        Class<?> clazz = compiler.loadClass("on.the.fly.UserProxy", results);
        // try instance:
        //User user = (User) clazz.newInstance();
    }
    static final String JAVA_SOURCE_CODE = "/* a single java source file */   "
            + "package on.the.fly;                                            "
            + "public class UserProxy {                     "
            + "    boolean _dirty = false;                                    "
            + "    public void setId(String id) {                             "
            + "        super.setId(id);                                       "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setName(String name) {                         "
            + "        super.setName(name);                                   "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setCreated(long created) {                     "
            + "        super.setCreated(created);                             "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setDirty(boolean dirty) {                      "
            + "        this._dirty = dirty;                                   "
            + "    }                                                          "
            + "    public boolean isDirty() {                                 "
            + "        return this._dirty;                                    "
            + "    }                                                          "
            + "}                                                              ";
    static final String JAVA_SOURCE_CODE_TESTNG ="import lombok.extern.slf4j.Slf4j;\n" +
            "import org.testng.annotations.Test;\n" +
            "\n" +
            "@Slf4j\n" +
            "public class MyTestNgSuiteTest {\n" +
            "    @Test\n" +
            "    public void test1(){\n" +
            "        log.info(\"test1()\");\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void test2(){\n" +
            "        log.info(\"test2()\");\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void test3(){\n" +
            "        log.info(\"test3()\");\n" +
            "    }\n" +
            "}\n";
}
