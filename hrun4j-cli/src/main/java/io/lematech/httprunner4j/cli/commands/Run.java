package io.lematech.httprunner4j.cli.commands;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.engine.TestNGEngine;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className The <code>run</code> command.
 * @description TODO
 * @created 2021/4/18 7:49 下午
 * @publicWechat lematech
 */
public class Run extends Command {

    @Option(name = "--testcase_paths", usage = "list of testcase path to execute", metaVar = "<testcase_paths>")
    List<File> testcasePaths = new ArrayList<>();

    @Override
    public String description() {
        return "Print run command information.";
    }

    @Option(name = "--verbose", usage = "show method and line number details")
    private boolean verbose = false;

    @Option(name = "--testjar", usage = "Specifies a jar file that contains aviator exp implemenets.")
    File testJar;

    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        initRunnerConfig();

        TestNGEngine.run();
        return 0;
    }

    private void initRunnerConfig() {
        if (Objects.isNull(testJar)) {
            RunnerConfig.getInstance().setWorkDirectory(new File("."));
        } else {
            if (!testJar.exists() || !testJar.isFile() || !FileUtil.extName(testJar).endsWith("jar")) {
                String exceptionMsg = String.format("testjar: %s is not exist,not directory or  must set .jar file path"
                        , FilesUtil.fileValidateAndGetCanonicalPath(testJar));
                throw new DefinedException(exceptionMsg);
            }
            File workFile = testJar.getParentFile();
            String workDirPath = FilesUtil.fileValidateAndGetCanonicalPath(workFile);
            MyLog.info("The workspace path：{}", workDirPath);
            Properties property = System.getProperties();
            property.setProperty("user.dir", workDirPath);
            RunnerConfig.getInstance().setWorkDirectory(new File(workDirPath));
        }

        if (testcasePaths.size() == 0) {
            String exceptionMsg = String.format("The test case path cannot be empty");
            throw new DefinedException(exceptionMsg);
        }
        List<File> canonicalTestCasePaths = new ArrayList<>();
        String workDirPath = FilesUtil.fileValidateAndGetCanonicalPath(RunnerConfig.getInstance().getWorkDirectory());
        FilesUtil.dirPath2pkgName(workDirPath);

        for (File caseFile : testcasePaths) {
            File canonicalCaseFile = caseFile;
            String caseFilePath = caseFile.getPath();
            FilesUtil.dirPath2pkgName(caseFilePath);
            if (!FileUtil.isAbsolutePath(caseFilePath)) {
                canonicalCaseFile = new File(workDirPath, caseFilePath);
            }
            if (Objects.isNull(canonicalCaseFile) || !canonicalCaseFile.exists()) {
                String exceptionMsg = String.format("Case file %s does not exist", FilesUtil.fileValidateAndGetCanonicalPath(caseFile));
                throw new DefinedException(exceptionMsg);
            }
            canonicalTestCasePaths.add(canonicalCaseFile);
        }
        RunnerConfig.getInstance().setTestCasePaths(testcasePaths);
        RunnerConfig.getInstance().setRunMode(1);
    }
}
