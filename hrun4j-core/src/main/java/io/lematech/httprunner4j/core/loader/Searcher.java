package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import java.io.File;
import java.net.URL;
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
    private RunnerConfig.RunMode runMode;
    /**
     * work directory
     */
    private String workDirectory;

    /**
     * testcase extension name
     */
    private String testCaseExtName;

    public Searcher() {
        runMode = RunnerConfig.getInstance().getRunMode();
        workDirectory = RunnerConfig.getInstance().getWorkDirectory().getAbsolutePath();
        testCaseExtName = RunnerConfig.getInstance().getTestCaseExtName();
    }

    public File quicklySearchFile(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            String exceptionMsg = String.format("FilePath can not null or empty");
            throw new DefinedException(exceptionMsg);
        }
        if (StrUtil.isEmpty(FileUtil.extName(filePath))) {
            filePath = filePath + Constant.DOT_PATH + testCaseExtName;
        }
        File searchFile = null;
        if (runMode == RunnerConfig.RunMode.CLI) {
            if (FileUtil.isAbsolutePath(filePath)) {
                searchFile = new File(filePath);
            } else {
                searchFile = new File(workDirectory, FilesUtil.filePathDecode(filePath));
            }
        } else if (runMode == RunnerConfig.RunMode.API) {
            if (!filePath.startsWith(File.separator)) {
                filePath = File.separator + filePath;
            }
            URL url = TestBase.class.getResource(filePath);
            if (Objects.isNull(url)) {
                String exceptionMsg = String.format("The file %s was not found under the resources", filePath);
                throw new DefinedException(exceptionMsg);
            }
            searchFile = new File(FilesUtil.filePathDecode(url.getPath()));
        }
        if (Objects.isNull(searchFile)) {
            String exceptionMsg = String.format("File %s is not exist", filePath);
            throw new DefinedException(exceptionMsg);
        }
        if (searchFile.exists() && searchFile.isFile()) {
            return searchFile;
        } else {
            String exceptionMsg = String.format("The file %s was not found in the current path %s", searchFile.getName(), FilesUtil.fileValidateAndGetCanonicalPath(searchFile.getParentFile()));
            throw new DefinedException(exceptionMsg);
        }
    }

}
