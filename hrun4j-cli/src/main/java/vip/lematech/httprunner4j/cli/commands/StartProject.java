package vip.lematech.httprunner4j.cli.commands;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import vip.lematech.httprunner4j.cli.constant.CliConstants;
import vip.lematech.httprunner4j.cli.service.impl.ProjectGeneratorImpl;
import vip.lematech.httprunner4j.cli.handler.Command;
import vip.lematech.httprunner4j.cli.model.scaffolding.ProjectInfo;
import vip.lematech.httprunner4j.cli.service.IProjectGenerator;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.config.RunnerConfig;
import vip.lematech.httprunner4j.helper.LogHelper;
import vip.lematech.httprunner4j.helper.JavaIdentifierHelper;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintWriter;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * The <code>startproject</code> command.
 */
public class StartProject extends Command {

    @Override
    public String description() {
        return "Print startproject command information.";
    }

    @Argument(usage = "Enter project name", metaVar = "<project_name>")
    String projectName;

    @Option(name = "--group_id", usage = "Specify maven project groupId.")
    String groupId = "vip.lematech.httprunner4j";

    @Option(name = "--version", usage = "Specify maven project version.")
    String version = "1.0.0-SNAPSHOT";

    @Option(name = "--type", usage = "Project type, default is httprunner4j POM type, support CLI/SRPINGBOOT ")
    String type = CliConstants.HTTPRUNNER4J_POM_TYPE;

    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        if (StrUtil.isEmpty(projectName)) {
            LogHelper.warn("Please enter a project name");
            return -1;
        }
        JavaIdentifierHelper.isValidJavaFullClassName(groupId);
        ProjectInfo projectInfo = new ProjectInfo(groupId, projectName
                , version, projectName, String.format("Demo project for %s", projectName));
        RunnerConfig.getInstance().setWorkDirectory(new File(Constant.DOT_PATH));
        String projectRoot = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory()) + File.separator;
        LogHelper.info("工作区路径：{}", projectRoot);
        IProjectGenerator projectGenerator = new ProjectGeneratorImpl();
        if (CliConstants.SRPINGBOOT_PROJECT_TYPE.equalsIgnoreCase(type)) {
            LogHelper.info("正在初始化SpringBoot项目信息");
            projectGenerator.springbootGenerator(projectRoot, projectInfo);
        } else if (CliConstants.HTTPRUNNER4J_CLI_TYPE.equalsIgnoreCase(type)) {
            LogHelper.info("正在初始化HttpRunner Cli项目信息");
            projectGenerator.cliGenerator(projectRoot, projectName);
        } else {
            LogHelper.info("正在初始化HttpRunner POM项目信息");
            projectGenerator.pomGenerator(projectRoot, projectInfo);
        }
        return 1;
    }

}
