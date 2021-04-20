package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.utils.RegularUtil;

import java.io.File;
import java.util.List;

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

   /* public String seekModelFileByCasePath(String filePath) {
        if (!StrUtil.isEmpty(filePath)) {
            if (filePath.startsWith(Constant.TEST_CASE_FILE_PATH)) {
                filePath = filePath.replaceFirst(Constant.TEST_CASE_FILE_PATH, "");
            } else if (filePath.startsWith(Constant.TEST_CASE_DIRECTORY_NAME)) {
                filePath = filePath.replaceFirst(Constant.TEST_CASE_DIRECTORY_NAME, "");
            }
            String extName = FileUtil.extName(filePath);
            String mainName = FileUtil.mainName(filePath);
            if (StrUtil.isEmpty(extName)) {
                extName = RunnerConfig.getInstance().getTestCaseExtName();
            } else {
                filePath = RegularUtil.replaceLast(filePath, mainName + Constant.DOT_PATH + extName, "");
                if (filePath.endsWith("/")) {
                    filePath = RegularUtil.replaceLast(filePath, "/", "");
                }
            }
            MyLog.debug("路径名：{},文件名：{}，扩展名：{}", RegularUtil.dirPath2pkgName(filePath), mainName, extName);
            return seekDataFileByRule(RegularUtil.dirPath2pkgName(filePath), mainName, extName);
        }
        return "";
    }
*/

    /**
     * search data file by relative path
     *
     * @param fileRelativePath
     * @return
     */
    public File searchDataFileByRelativePath(String fileRelativePath) {
        StringBuffer filePath = new StringBuffer();
        if (StrUtil.isEmpty(fileRelativePath)) {
            String exceptionMsg = String.format("fileRelativePath can not null or empty");
            throw new DefinedException(exceptionMsg);
        }
        if (runMode == 1) {
            filePath.append(workDirectory.endsWith("/") ? workDirectory : workDirectory + "/");
            filePath.append(fileRelativePath.endsWith(testCaseExtName) ? fileRelativePath : fileRelativePath + Constant.DOT_PATH + testCaseExtName);
            File dataFilePath = new File(filePath.toString());
            if (dataFilePath.exists() && dataFilePath.isFile()) {
                return dataFilePath;
            } else {
                String exceptionMsg = String.format("in %s path,not found  %s", dataFilePath.getAbsolutePath(), fileRelativePath);
                throw new DefinedException(exceptionMsg);
            }
        }
        return null;
    }

    /**
     * @param pkgClassName
     * @param testCaseName
     * @return
     */
    public File seekDataFileByRule(String pkgClassName, String testCaseName) {
        if (StrUtil.isEmpty(pkgClassName) || StrUtil.isEmpty(testCaseName)) {
            String exceptionMsg = String.format("pkgClassName or testCaseName can not null or empty");
            throw new DefinedException(exceptionMsg);
        }
        if (runMode == 1) {
            File dataFilePath = new File(pkgClassNameToFilePath(pkgClassName, testCaseName));
            if (dataFilePath.exists() && dataFilePath.isFile()) {
                return dataFilePath;
            } else {
                String exceptionMsg = String.format("in %s path,not found  %s.%s", dataFilePath.getAbsolutePath(), testCaseName, testCaseExtName);
                throw new DefinedException(exceptionMsg);
            }
        } else if (runMode == 2) {

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
                .append(methodName)
                .append(Constant.DOT_PATH)
                .append(testCaseExtName);
        return filePath.toString();
    }
}
