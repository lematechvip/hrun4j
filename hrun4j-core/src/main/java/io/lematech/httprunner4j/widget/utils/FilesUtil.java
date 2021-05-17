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


    public static void checkFileExists(File file) {
        if (Objects.isNull(file)) {
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
