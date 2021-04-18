package io.lematech.httprunner4j.cli.commands;

import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.core.engine.TestNGEngine;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public int execute(PrintWriter out, PrintWriter err) throws Exception {
        RunnerConfig.getInstance().setExecutePaths(testcasePaths);
        RunnerConfig.getInstance().setRunMode(1);
        TestNGEngine.run();
        return 0;
    }
}
