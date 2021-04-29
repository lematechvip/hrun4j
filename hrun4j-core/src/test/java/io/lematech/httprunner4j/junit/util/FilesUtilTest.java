package io.lematech.httprunner4j.junit.util;

import groovy.lang.GroovyClassLoader;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className FilesUtilTest
 * @description TODO
 * @created 2021/4/20 1:51 下午
 * @publicWechat lematech
 */
public class FilesUtilTest {
    @Test
    public void testFilesUtil() {
      /*  List<File> fileList = new ArrayList<>();
        fileList.add(new File("."));
        FilesUtil.fileList2TestClass(fileList);*/
        File file = new File(".");
        MyLog.info("文件路径：{}", file.getName());

        MyLog.info("文件路径：{}", file.getAbsolutePath());
        MyLog.info("文件路径：{}", file.getPath());
        MyLog.info("文件路径：{}", file.getParent());
        try {
            MyLog.info("文件路径：{}", file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        groovyClassLoader.addClasspath("/Users/arkhe/Desktop/resources/hrun4j-cli.jar");
        // groovyClassLoader.ge
        Class[] clzs = groovyClassLoader.getLoadedClasses();
        for (Class clz : clzs) {
            MyLog.info("包名：{},类名：{}", clz.getPackage(), clz.getName());
        }
    }


}
