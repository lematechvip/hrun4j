package io.lematech.httprunner4j.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import lombok.Data;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Searcher
 * @description seracher file by file path
 * @created 2021/4/19 4:12 下午
 * @publicWechat lematech
 */
@Data
public class Searcher {

    /**
     * run mode
     * supports cli 、api integration
     */
    private RunnerConfig.RunMode runMode;

    /**
     * work directory
     */
    private String workDirectory;


    public Searcher() {
        runMode = RunnerConfig.getInstance().getRunMode();
        workDirectory = RunnerConfig.getInstance().getWorkDirectory().getAbsolutePath();
    }

    /**
     * quickly search file by file path
     *
     * @param filePath
     * @return
     */
    public File quicklySearchFile(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            String exceptionMsg = String.format("FilePath can not null or empty");
            throw new DefinedException(exceptionMsg);
        }
        if (StrUtil.isEmpty(FileUtil.extName(filePath))) {
            filePath = filePath + Constant.DOT_PATH + RunnerConfig.getInstance().getTestCaseExtName();
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
        if (!searchFile.exists() || !searchFile.isFile()) {
            String exceptionMsg = String.format("The file %s was not found in the current path %s", searchFile.getName(), FileUtil.getAbsolutePath(searchFile.getParentFile()));
            throw new DefinedException(exceptionMsg);
        }
        MyLog.debug("Run mode: {}, Test case path {}", runMode, FileUtil.getAbsolutePath(searchFile));
        return searchFile;
    }

    /**
     * @param dataFileType
     * @param directoryName
     * @return
     */
    public String spliceFilePath(String dataFileType, String directoryName) {
        if (!dataFileType.startsWith(directoryName + File.separator) &&
                !dataFileType.startsWith(File.separator + directoryName + File.separator)) {
            dataFileType = directoryName + File.separator + dataFileType;
        }
        if (FileUtil.isAbsolutePath(dataFileType)) {
            dataFileType = Constant.DOT_PATH + dataFileType;
        }
        if (StrUtil.isEmpty(FileUtil.extName(dataFileType))) {
            dataFileType = dataFileType + Constant.DOT_PATH + RunnerConfig.getInstance().getTestCaseExtName();
        }
        return dataFileType;
    }


}
