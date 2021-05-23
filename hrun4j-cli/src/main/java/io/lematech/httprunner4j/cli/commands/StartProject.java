package io.lematech.httprunner4j.cli.commands;


import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.cli.scaffolding.domain.model.ProjectInfo;
import io.lematech.httprunner4j.cli.scaffolding.domain.service.ProjectGeneratorImpl;

import java.io.PrintWriter;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Har2Yml
 * @description The <code>startproject</code> command.
 * @created 2021/4/18 7:53 下午
 * @publicWechat lematech
 */
public class StartProject extends Command {

    @Override
    public String description() {
        return "Print har2yml command information.";
    }

    @Override
    public int execute(PrintWriter out, PrintWriter err) throws Exception {
        ProjectInfo projectInfo = new ProjectInfo(
                "io.lematech.httprunner4j",
                "firstProject",
                "1.0.0-SNAPSHOT",
                "firstProject",
                "Demo project for HttpRunner4j"
        );
        ProjectGeneratorImpl projectGenerator = new ProjectGeneratorImpl();
        projectGenerator.generator(projectInfo);
        return 1;
    }

}
