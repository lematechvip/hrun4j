package io.lematech.httprunner4j.widget.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.log.MyLog;

import java.io.File;
import java.io.IOException;
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
            return null;
        }

        if (dirPath.startsWith(Constant.DOT_PATH)) {
            dirPath = dirPath.replaceFirst("\\.", "");
        }
        pkgName.append(dirPath.replaceAll("/", Constant.DOT_PATH));
        return pkgName.toString();
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
            if (!file.exists()) {
                String exceptionMsg = null;
                try {
                    exceptionMsg = String.format("file: %s is not exist", file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new DefinedException(exceptionMsg);
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

    private static void fileToTestClassMap(Map<String, Set<String>> fileTestClassMap, File file) {
        String extName = FileUtil.extName(file);
        if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)
                || Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
            String fileMainName = JavaIdentifierUtil.toValidJavaIdentifier(FileNameUtil.mainName(file.getName()), 0);
            StringBuffer pkgName = new StringBuffer();
            pkgName.append(RunnerConfig.getInstance().getPkgName());
            try {
                if (filePathFlag) {
                    pkgName.append("_");
                    String workDirPath = RunnerConfig.getInstance().getWorkDirectory().getCanonicalPath();
                    String relativePath = file.getParentFile().getCanonicalPath().replace(workDirPath, "");
                    pkgName.append(FilesUtil.dirPath2pkgName(relativePath));
                } else {
                    pkgName.append(FilesUtil.dirPath2pkgName(file.getCanonicalPath()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuffer pkgTestClassMetaInfo = new StringBuffer(JavaIdentifierUtil.toValidJavaIdentifier(pkgName.toString(), 1));
            String folderName = file.getParentFile().getName();
            String testClassName = StrUtil.upperFirst(StrUtil.toCamelCase(String.format("%sTest", JavaIdentifierUtil.toValidJavaIdentifier(folderName, 0))));
            pkgTestClassMetaInfo.append(Constant.DOT_PATH).append(testClassName);
            String fullTestClassName = pkgTestClassMetaInfo.toString();
            MyLog.debug("full test class name is：{},class file is：{},method name is：{}", fullTestClassName, testClassName, fileMainName);
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
