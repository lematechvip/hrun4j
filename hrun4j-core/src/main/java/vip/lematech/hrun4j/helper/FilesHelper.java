package vip.lematech.hrun4j.helper;


import cn.hutool.core.util.StrUtil;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class FilesHelper {

    /**
     * file path decode,support zh
     *
     * @param filePath file path
     * @return file path
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
     * @param dirPath directory path
     * @return package name
     */
    public static String dirPath2pkgName(String dirPath) {

        if (StrUtil.isEmpty(dirPath)) {
            return "";
        }

        char[] dirPaths = dirPath.toCharArray();
        StringBuffer pkgName = new StringBuffer();
        boolean preDotFlag = true;
        for(int index =0;index<dirPaths.length;index++){
            char tmpChar = dirPaths[index];
            if(tmpChar == '.'){
                continue;
            }
            if(tmpChar == File.separatorChar){
                if(preDotFlag){
                    preDotFlag = false;
                    continue;
                }
                pkgName.append(Constant.DOT_PATH);
            }else{
                pkgName.append(tmpChar);
            }

        }
        String packageName = pkgName.toString();
        if (!JavaIdentifierHelper.isValidJavaFullClassName(packageName)) {
            throw new DefinedException(JavaIdentifierHelper.validateIdentifierName(packageName));
        }
        return packageName;
    }


    /**
     * @param pkgPath The package path
     * @return file path
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
            String exceptionMsg = String.format("File %s does not exist on device", file.getAbsolutePath());
            throw new DefinedException(exceptionMsg);
        } else if (!file.isFile()) {
            String exceptionMsg = String.format("File does not represent a valid file", file.getAbsolutePath());
            throw new DefinedException(exceptionMsg);
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
