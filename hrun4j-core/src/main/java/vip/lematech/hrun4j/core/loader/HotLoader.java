package vip.lematech.hrun4j.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.itranswarp.compiler.JavaStringCompiler;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.helper.LogHelper;
import org.testng.collections.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Hot loader for loading classes files
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class HotLoader {
    public static Set<Class> hotLoaderClasses = new HashSet<>();
    private static JavaStringCompiler compiler;

    public static synchronized JavaStringCompiler getInstance() {
        if (compiler == null) {
            compiler = new JavaStringCompiler();
        }
        return compiler;
    }

    /**
     * hot load self-defined class
     * @param pkgName The package name
     * @param className The class name
     * @param source The source code
     * @return The generated class
     */
    public static synchronized Class<?> hotLoadClass(String pkgName, String className, String source){
        if(StrUtil.isEmpty(className)||StrUtil.isEmpty(source)){
            throw new DefinedException("hot load class occur exception: className or source is empty");
        }
        String replaceSource = replaceSourceParameters(pkgName,className,source);
        String javaFileName = String.format("%s.java",className);
        String pkgClassName = String.format("%s.%s",pkgName,className);
        Class<?> clazz ;
        try {
            LogHelper.debug("initializing class[{}] ", pkgClassName);
            Map<String, byte[]> results = getInstance().compile(javaFileName, replaceSource);
            clazz = compiler.loadClass(pkgClassName, results);
            LogHelper.debug("hot load class[{}] finished", pkgClassName);
            hotLoaderClasses.add(clazz);
        } catch (IOException e) {
            String exceptionMsg = String.format("compile %s occur exception: ",javaFileName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (ClassNotFoundException e) {
            String exceptionMsg = String.format("class %s not found exception: ", pkgClassName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return clazz;
    }

    private static void traverseSrcJava(String rootPath,File file,List<Map<String,String>> javaMetaInfos){

        if(!file.exists()){
            String msg = String.format("file %s is not exist",file.getName());
            throw new DefinedException(msg);
        }

        File []files = file.listFiles();
        for(File javaFile : files){
            if(javaFile.isFile() && javaFile.getName().endsWith(".java")){
                Map<String,String> javaMetaInfo = Maps.newHashMap();
                String pkgPath = file.getParent().replace(rootPath,"");
                String []pkgNames = dirPath2pkgName(pkgPath);
                StringBuffer pkgNameStr = new StringBuffer();
                for(String pkgPortionName:pkgNames){
                    if(StrUtil.isEmpty(pkgPortionName)){
                        continue;
                    }
                    pkgNameStr.append(Constant.DOT_PATH).append(pkgPortionName);
                }
                String className = FileNameUtil.mainName(file.getName());
                String pkgName = pkgNameStr.toString();
                javaMetaInfo.put("pkgName",pkgName);
                javaMetaInfo.put("className",className);
                javaMetaInfo.put("source",getJavaFileContent(javaFile));
                javaMetaInfos.add(javaMetaInfo);
                LogHelper.debug("package name is {}, class name is {}", pkgName, className);
            }else{
                traverseSrcJava(rootPath,javaFile,javaMetaInfos);
            }
        }
    }

    /**
     * get java file source content
     * @param file
     * @return
     */
    private static String getJavaFileContent(File file){
        BufferedReader javaFileReader = FileUtil.getReader(file,"UTF-8");
        String lineInfo ;
        StringBuffer javaFileContent = new StringBuffer();
        try{
            while ((lineInfo = javaFileReader.readLine()) != null) {
                javaFileContent.append(lineInfo);
            }
        }catch (IOException ioException){
            String exceptionMsg = String.format("file {} occur io exception",file.getName());
            throw new DefinedException(exceptionMsg);
        }finally {
            try {
                javaFileReader.close();
            } catch (IOException ioException) {
                String exceptionMsg = String.format("close file {} occur exception",file.getName());
                throw new DefinedException(exceptionMsg);
            }
        }
        return javaFileContent.toString();
    }

    /**
     * src path transfer package name
     * @param pkgPath
     * @return
     */
    private static String[] dirPath2pkgName(String pkgPath){
        if(StrUtil.isEmpty(pkgPath)){
            return new String[]{};
        }
        String []pkgName = pkgPath.split("/");
        return pkgName;
    }

    /**
     * replace source string parameters
     * @param pkgName
     * @param className
     * @param source
     * @return
     */
    private static String replaceSourceParameters(String pkgName,String className,String source){
        String replaceSource = source;
        LogHelper.debug("init source info : {}", source);
        if(source.contains("$pkgName")){
            replaceSource = source.replace("$pkgName",pkgName);
        }
        if(source.contains("$className")){
            replaceSource = replaceSource.replace("className",className);
        }
        LogHelper.debug("replace source parameters info : {}", replaceSource);
        return replaceSource;
    }
}
