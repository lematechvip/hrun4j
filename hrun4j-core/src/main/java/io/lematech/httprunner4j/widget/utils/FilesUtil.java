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
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className FileUtil
 * @description fileUtil
 * @created 2021/4/20 1:21 下午
 * @publicWechat lematech
 */
public class FilesUtil {

    /**
     * file path decode,support zh
     *
     * @param filePath
     * @return
     */
    public static String filePathDecode(String filePath) {
        String decodePath;
        try {
            decodePath = URLDecoder.decode(filePath, Constant.CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            String exceptionMsg = String.format("The file %s encoding is abnormal,Exception information:%s", filePath, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return decodePath;
    }

    /**
     * Path subcontract name
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
        if (dirPath.startsWith(File.separator)) {
            dirPath = dirPath.replaceFirst(File.separator, "");
        }
        if (dirPath.endsWith(File.separator)) {
            dirPath = SmallUtil.replaceLast(dirPath, File.separator, "");
        }
        if (dirPath.contains(File.separator)) {
            dirPath = dirPath.replaceAll(File.separator, Constant.DOT_PATH);
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
            String exceptionMsg = "The package name cannot be empty";
            throw new DefinedException(exceptionMsg);
        }
        return pkgPath.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
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
            FilesUtil.fileValidate(file);
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
    public static String getCanonicalPath(File file) {
        String fileFullPath = null;
        try {
            fileFullPath = file.getCanonicalPath();
        } catch (IOException e) {
            String exceptionMsg = String.format("There is an exception in the canonical path of the file %s, and the exception information is::%s", file.getAbsolutePath(), e.getMessage());
            new DefinedException(exceptionMsg);
        }
        return fileFullPath;
    }

    public static File fileValidate(File file) {
        if (Objects.isNull(file) || !file.exists()) {
            String exceptionMsg = String.format("File %s does not exist", getCanonicalPath(file));
            throw new DefinedException(exceptionMsg);
        }
        return file;
    }

    private static void fileToTestClassMap(Map<String, Set<String>> fileTestClassMap, File file) {
        String extName = FileUtil.extName(file);
        if (Constant.SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME.equalsIgnoreCase(extName)
                || Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equalsIgnoreCase(extName)) {
            String fileMainName = FileNameUtil.mainName(file.getName());
            String fileCanonicalPath = getCanonicalPath(file);
            if (!JavaIdentifierUtil.isValidJavaIdentifier(fileMainName)) {
                String exceptionMsg = String.format("File name:%s  does not match Java identifier(No special characters are allowed. The first character must be '$',' _ ', 'letter'. No special characters such as' - ', 'space', '/' are allowed), in the path: ", fileCanonicalPath);
                throw new DefinedException(exceptionMsg);
            }
            String fileParentCanonicalPath = getCanonicalPath(file.getParentFile());
            StringBuffer pkgName = new StringBuffer();
            pkgName.append(RunnerConfig.getInstance().getPkgName());
            String filePath;
            if (filePathFlag) {
                pkgName.append("_");
                String workDirPath = getCanonicalPath(RunnerConfig.getInstance().getWorkDirectory());
                filePath = fileParentCanonicalPath.replaceFirst(workDirPath, "");
            } else {
                filePath = fileParentCanonicalPath;
            }
            String transferPackageName = FilesUtil.dirPath2pkgName(filePath);
            String validatePackageInfo = JavaIdentifierUtil.validateIdentifierName(transferPackageName);
            if (!StrUtil.isEmpty(validatePackageInfo)) {
                throw new DefinedException(validatePackageInfo);
            }
            pkgName.append(Constant.DOT_PATH).append(transferPackageName);
            StringBuffer pkgTestClassMetaInfo = new StringBuffer(pkgName.toString());
            String folderName = file.getParentFile().getName();
            String testClassName = StrUtil.upperFirst(StrUtil.toCamelCase(String.format("%sTest", folderName)));
            pkgTestClassMetaInfo.append(Constant.DOT_PATH).append(testClassName);
            String fullTestClassName = pkgTestClassMetaInfo.toString();
            MyLog.debug("Complete class package name:{}, filename: {},method name: {}", fullTestClassName, testClassName, fileMainName);
            if (fileTestClassMap.containsKey(fullTestClassName)) {
                Set<String> testClassList = fileTestClassMap.get(fullTestClassName);
                testClassList.add(fileMainName);
            } else {
                Set<String> testClassList = new HashSet<>();
                testClassList.add(fileMainName);
                fileTestClassMap.put(fullTestClassName, testClassList);
            }
        } else {
            MyLog.debug("Current file {}.{} format support, only support YML or JSON file suffix", file.getPath(), file.getName());
        }
    }

    public static void checkFileExists(String absoluteFilePath) {
        if (StrUtil.isEmpty(absoluteFilePath)) {
            throw new DefinedException("Absolute file path cannot be null/empty");
        } else {
            File file = new File(absoluteFilePath);
            checkFileExists(file);
        }
    }

    public static void checkFileExists(File file) {
        if (Objects.isNull(null)) {
            throw new DefinedException("File instance cannot be null");
        } else if (!file.exists()) {
            throw new DefinedException("File does not exist on device");
        } else if (!file.isFile()) {
            throw new DefinedException("File does not represent a valid file");
        }
    }

    public static void checkDirectoryExists(String absoluteDirPath) {
        if (StrUtil.isEmpty(absoluteDirPath)) {
            throw new DefinedException("Absolute dir path cannot be null/empty");
        } else {
            File file = new File(absoluteDirPath);
            checkDirectoryExists(file);
        }
    }

    public static void checkDirectoryExists(File directory) {
        if (directory == null) {
            throw new DefinedException("File instance cannot be null");
        } else if (!directory.exists()) {
            throw new DefinedException("Directory does not exist on device");
        } else if (!directory.isFile()) {
            throw new DefinedException("File does not represent a valid directory");
        }
    }
}
