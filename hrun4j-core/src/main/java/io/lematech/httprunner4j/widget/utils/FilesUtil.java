package io.lematech.httprunner4j.widget.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.log.MyLog;

import java.io.*;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className FileUtil
 * @description TODO
 * @created 2021/4/20 1:21 下午
 * @publicWechat lematech
 */
public class FilesUtil {
    /**
     * 路径转包名
     *
     * @param dirPath
     * @return
     */
    public static String dirPath2pkgName(String dirPath) {
        StringBuffer pkgName = new StringBuffer();
        if (StrUtil.isEmpty(dirPath)) {
            return "";
        }
        if (dirPath.startsWith(Constant.DOT_PATH)) {
            dirPath = dirPath.replaceFirst("\\.", "");
        }
        if (dirPath.startsWith("/")) {
            dirPath = dirPath.replaceFirst("/", "");
        }
        if (dirPath.endsWith("/")) {
            dirPath = RegularUtil.replaceLast(dirPath, "/", "");
        }
        if (dirPath.contains("/")) {
            dirPath = dirPath.replaceAll("/", Constant.DOT_PATH);
        }
        pkgName.append(dirPath);
        String packageName = pkgName.toString();
        if (!JavaIdentifierUtil.isValidJavaFullClassName(packageName)) {
            throw new DefinedException(JavaIdentifierUtil.validateIdentifierName(packageName));
        }
        return packageName;
    }


    /**
     * @param pkgPath
     * @return
     */
    public static String pkgPath2DirPath(String pkgPath) {
        if (StrUtil.isEmpty(pkgPath)) {
            return null;
        }
        return pkgPath.replaceAll("\\.", "/");
    }

    /**
     * @param listFile
     * @return
     */
    public static Map<String, Set<String>> fileList2TestClass(List<File> listFile) {
        Map<String, Set<String>> fileTestClassMap = Maps.newHashMap();
        for (File file : listFile) {
            if (FileUtil.isAbsolutePath(file.getPath())) {
                filePathFlag = false;
            }
            if (filePathFlag) {
                file = new File(RunnerConfig.getInstance().getWorkDirectory().getPath(), file.getPath());
            }
            fileTraverse(file, fileTestClassMap);
        }

        return fileTestClassMap;
    }


    /**
     * file path flag , default true is relative path, otherwise abosolute path
     */
    private static boolean filePathFlag = true;

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
     * get canonical path
     *
     * @param file
     * @return
     */
    public static String fileValidateAndGetCanonicalPath(File file) {
        if (Objects.isNull(file) || !file.exists()) {
            String exceptionMsg = String.format("file %s does not exist", file.getAbsolutePath());
            throw new DefinedException(exceptionMsg);
        }
        String fileFullPath = null;
        try {
            fileFullPath = file.getCanonicalPath();
        } catch (IOException e) {
            String exceptionMsg = String.format("file %s get canonical path occur exception: %s", file.getAbsolutePath(), e.getMessage());
            new DefinedException(exceptionMsg);
        }
        return fileFullPath;
    }

    private static void fileToTestClassMap(Map<String, Set<String>> fileTestClassMap, File file) {
        String extName = FileUtil.extName(file);
        if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)
                || Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
            String fileMainName = FileNameUtil.mainName(file.getName());
            String fileCanonicalPath = fileValidateAndGetCanonicalPath(file);
            if (!JavaIdentifierUtil.isValidJavaIdentifier(fileMainName)) {
                String exceptionMsg = String.format("file name:%s  does not match Java identifier(No special characters are allowed. The first character must be '$',' _ ', 'letter'. No special characters such as' - ', 'space', '/' are allowed), in the path: ", fileCanonicalPath);
                throw new DefinedException(exceptionMsg);
            }
            String fileParentCanonicalPath = fileValidateAndGetCanonicalPath(file.getParentFile());
            StringBuffer pkgName = new StringBuffer();
            pkgName.append(RunnerConfig.getInstance().getPkgName());
            String filePath;
            if (filePathFlag) {
                pkgName.append("_");
                String workDirPath = fileValidateAndGetCanonicalPath(RunnerConfig.getInstance().getWorkDirectory());
                filePath = fileParentCanonicalPath.replaceFirst(workDirPath, "");
            } else {
                pkgName.append(Constant.DOT_PATH);
                filePath = fileParentCanonicalPath;
            }
            String transferPackageName = FilesUtil.dirPath2pkgName(filePath);
            String validatePackageInfo = JavaIdentifierUtil.validateIdentifierName(transferPackageName);
            if (!StrUtil.isEmpty(validatePackageInfo)) {
                throw new DefinedException(validatePackageInfo);
            }
            pkgName.append(transferPackageName);
            StringBuffer pkgTestClassMetaInfo = new StringBuffer(pkgName.toString());
            String folderName = file.getParentFile().getName();
            String testClassName = StrUtil.upperFirst(StrUtil.toCamelCase(String.format("%sTest", folderName)));
            pkgTestClassMetaInfo.append(Constant.DOT_PATH).append(testClassName);
            String fullTestClassName = pkgTestClassMetaInfo.toString();
            MyLog.info("full test class name is：{},class file is：{},method name is：{}", fullTestClassName, testClassName, fileMainName);
            if (fileTestClassMap.containsKey(fullTestClassName)) {
                Set<String> testClassList = fileTestClassMap.get(fullTestClassName);
                testClassList.add(fileMainName);
            } else {
                Set<String> testClassList = new HashSet<>();
                testClassList.add(fileMainName);
                fileTestClassMap.put(fullTestClassName, testClassList);
            }
        } else {
            MyLog.debug("in pkgPath {} file {} not support,only support .json or.yml suffix", file.getPath(), file.getName());
        }
    }

}
