package io.lematech.httprunner4j;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.itranswarp.compiler.JavaStringCompiler;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;


@Slf4j
public class HotLoader {
    public static Set<Class> hotLoaderClasses = new HashSet<>();
    private static JavaStringCompiler compiler;
    public static synchronized JavaStringCompiler getInstance(){
        if(compiler == null){
            compiler = new JavaStringCompiler();
        }
        return compiler;
    }

    /**
     * hot load self-defined class
     * @param pkgName
     * @param className
     * @param source
     * @return
     */
    public static synchronized Class<?> hotLoadClass(String pkgName,String className,String source){
        if(StrUtil.isEmpty(className)||StrUtil.isEmpty(source)){
            throw new DefinedException("hot load class occur exception: className or source is empty");
        }
        String replaceSource = replaceSourceParameters(pkgName,className,source);
        String javaFileName = String.format("%s.java",className);
        String pkgClassName = String.format("%s.%s",pkgName,className);
        Class<?> clazz ;
        try {
            log.debug("initializing class[{}] ",pkgClassName);
            Map<String, byte[]> results = getInstance().compile(javaFileName, replaceSource);
            clazz = compiler.loadClass(pkgClassName, results);
            log.debug("hot load class[{}] finished",pkgClassName);
            //todo：考虑常驻内存及out of Memory 可能
            hotLoaderClasses.add(clazz);
        } catch (IOException e) {
            String exceptionMsg = String.format("compile %s occur exception: ",javaFileName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (ClassNotFoundException e) {
            String exceptionMsg = String.format("class %s not found exception",pkgClassName);
            throw new DefinedException(exceptionMsg);
        }
        return clazz;
    }

    /**
     *  by src path hot load directory java file
     * @param srcPath
     * @return
     */
    public static List<Class<?>> hotLoadSrcDirectoryJava(String srcPath){
        List<Class<?>> classes = new ArrayList<>();
        File file;
        if(StrUtil.isEmpty(srcPath)){
            file = new File(Constant.DOT_PATH);
            log.warn("src path is empty ,file path is set current path.");
        }else{
            file = new File(srcPath);
        }
        String rootPath = file.getAbsolutePath();
        List<Map<String,String>> javaMetaInfos = new ArrayList<>();
        traverseSrcJava(rootPath,file,javaMetaInfos);
        for(Map<String,String> javaMetaInfo : javaMetaInfos){
            String pkgName = javaMetaInfo.get("pkgName");
            String className = javaMetaInfo.get("className");
            String source = javaMetaInfo.get("source");
            Class<?> javaClass = hotLoadClass(pkgName,className,source);
            classes.add(javaClass);
        }
        return classes;
    }

    /**
     * traverse path files
     * @param rootPath
     * @param file
     * @param javaMetaInfos
     */
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
                log.debug("package name is {}, class name is {}",pkgName,className);
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
        log.debug("init source info : {}",source);
        if(source.contains("$pkgName")){
            replaceSource = source.replace("$pkgName",pkgName);
        }
        if(source.contains("$className")){
            replaceSource = replaceSource.replace("className",className);
        }
        log.debug("replace source parameters info : {}",replaceSource);
        return replaceSource;
    }
}
