package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.utils.RegularUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Searcher
 * @description TODO
 * @created 2021/4/19 4:12 下午
 * @publicWechat lematech
 */
public class Searcher {

    /**
     * run model
     * default 0： Standard Java project
     * 1： non Standard Java project
     * 2: platform model
     */
    private Integer runMode;
    /**
     * work directory
     */
    private String workDirectory;
    /**
     * package name
     */
    private String pkgName;
    /**
     * testcase extension name
     */
    private String testCaseExtName;

    public Searcher() {
        runMode = RunnerConfig.getInstance().getRunMode();
        workDirectory = RunnerConfig.getInstance().getWorkDirectory().getAbsolutePath();
        pkgName = RunnerConfig.getInstance().getPkgName();
        testCaseExtName = RunnerConfig.getInstance().getTestCaseExtName();
    }


    /**
     * search data file by relative path
     *
     * @param fileRelativePath
     * @return
     */
    public File searchDataFileByRelativePath(String fileRelativePath) {
        if (runMode == 1) {
            fileRelativePath = (workDirectory.endsWith("/") ? workDirectory : workDirectory + File.separator) + fileRelativePath;
        }
        String fileName = FileUtil.getName(fileRelativePath);
        String filePathName = RegularUtil.replaceLast(fileRelativePath, fileName, "");
        String pkgClassName = FilesUtil.dirPath2pkgName(filePathName);
        return searchDataFileByRule(pkgClassName, fileName);
    }

    /**
     * @param pkgClassName
     * @param testCaseName
     * @return
     */
    public File searchDataFileByRule(String pkgClassName, String testCaseName) {
        if (StrUtil.isEmpty(pkgClassName) || StrUtil.isEmpty(testCaseName)) {
            String exceptionMsg = String.format("pkgClassName or testCaseName can not null or empty");
            throw new DefinedException(exceptionMsg);
        }

        File dataFilePath = new File(pkgClassNameToFilePath(pkgClassName.replaceAll("_", "-"), testCaseName));
        if (runMode == 1) {
            if (dataFilePath.exists() && dataFilePath.isFile()) {
                return dataFilePath;
            } else {
                String exceptionMsg = String.format("in %s path,not found  %s.%s", dataFilePath.getParentFile().getAbsolutePath(), testCaseName, testCaseExtName);
                throw new DefinedException(exceptionMsg);
            }
        } else if (runMode == 2) {
            String filePath = dataFilePath.getPath();
            if (!filePath.startsWith("/")) {
                filePath = File.separator + filePath;
            }
            URL url = this.getClass().getResource(filePath);
            if (Objects.isNull(url)) {
                String exceptionMsg = String.format("in %s path,not found  %s", dataFilePath.getAbsolutePath(), testCaseName);
                throw new DefinedException(exceptionMsg);
            }
            // 处理中文乱码
            String decodePath = null;
            try {
                decodePath = URLDecoder.decode(url.getPath(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                String exceptionMsg = String.format("url %s decode occur error", url.getPath());
                throw new DefinedException(exceptionMsg);
            }
            return new File(decodePath);

        }
        return null;
    }

    /**
     * @param pkgClassName
     * @param methodName
     * @return
     */
    private String pkgClassNameToFilePath(String pkgClassName, String methodName) {
        StringBuffer filePath = new StringBuffer();
        String removePrefixPkgClassName = pkgClassName.replaceFirst(pkgName, "");
        if (removePrefixPkgClassName.startsWith("_")) {
            removePrefixPkgClassName = removePrefixPkgClassName.replaceFirst("_", workDirectory);
        }
        String removeSuffixTestName = RegularUtil.replaceLast(removePrefixPkgClassName, "Test", "");
        String caseDirPath = FilesUtil.pkgPath2DirPath(removeSuffixTestName);
        filePath.append(caseDirPath)
                .append(File.separator)
                .append(methodName);
        if (StrUtil.isEmpty(FileUtil.extName(methodName))) {
            filePath.append(Constant.DOT_PATH).append(testCaseExtName);
        }
        return filePath.toString();
    }
}
