package io.lematech.httprunner4j.cli.commands;

import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.engine.TestNGEngine;
import io.lematech.httprunner4j.widget.log.MyLog;
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
            if (!testJar.exists()) {
                String exceptionMsg = String.format("file: %s is not exist", testJar.getAbsolutePath());
                throw new DefinedException(exceptionMsg);
            }
            RunnerConfig.getInstance().setWorkDirectory(testJar);
            Properties property = System.getProperties();
            property.setProperty("user.dir", testJar.getAbsolutePath());
        }
        RunnerConfig.getInstance().setTestCasePaths(testcasePaths);
        RunnerConfig.getInstance().setRunMode(1);
    }
}
