package io.lematech.httprunner4j.cli.commands;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.cli.handler.Command;
import io.lematech.httprunner4j.cli.model.scaffolding.ProjectInfo;
import io.lematech.httprunner4j.cli.service.IProjectGenerator;
import io.lematech.httprunner4j.cli.service.impl.ProjectGeneratorImpl;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import java.io.File;
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
        return "Print startproject command information.";
    }

    @Argument(usage = "Enter project name", metaVar = "<project_name>")
    String projectName;

    @Option(name = "--group_id", usage = "Specify maven project groupId.")
    String groupId = "io.lematech.httprunner4j";

    @Option(name = "--version", usage = "Specify maven project version.")
    String version = "1.0.0-SNAPSHOT";

    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        if (StrUtil.isEmpty(projectName)) {
            MyLog.warn("Please enter a project name");
            return -1;
        }

        JavaIdentifierUtil.isValidJavaFullClassName(groupId);
        ProjectInfo projectInfo = new ProjectInfo(groupId, projectName
                , version, projectName, String.format("Demo project for %s", projectName));
        RunnerConfig.getInstance().setWorkDirectory(new File(Constant.DOT_PATH));
        String projectRoot = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory()) + File.separator;
        MyLog.info("工作区路径：{}", projectRoot);
        IProjectGenerator projectGenerator = new ProjectGeneratorImpl();
        projectGenerator.springbootGenerator(projectRoot, projectInfo);

        return 1;
    }

}
