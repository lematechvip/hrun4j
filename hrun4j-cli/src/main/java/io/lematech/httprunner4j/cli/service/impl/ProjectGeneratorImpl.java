package io.lematech.httprunner4j.cli.service.impl;

import cn.hutool.core.io.FileUtil;
import io.lematech.httprunner4j.cli.constant.CliConstants;
import io.lematech.httprunner4j.cli.model.scaffolding.ApplicationInfo;
import io.lematech.httprunner4j.cli.model.scaffolding.ProjectInfo;
import io.lematech.httprunner4j.cli.service.IProjectGenerator;
import io.lematech.httprunner4j.cli.testsuite.TemplateEngine;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.JavaIdentifierUtil;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.Arrays;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public class ProjectGeneratorImpl implements IProjectGenerator {


    @Override
    public void springbootGenerator(String projectRoot, ProjectInfo projectInfo) {
        String packageName = generatePackageName(projectInfo);
        String packagePath = packageName.replace(Constant.DOT_PATH, File.separator);
        ApplicationInfo applicationInfo = getApplicationInfo(projectInfo);
        String className = applicationInfo.getClassName();
        String artifactId = projectInfo.getArtifactId();
        String filePath = String.format("%s%s/src/main/java/%s%s%s.java", projectRoot, artifactId, packagePath, File.separator, className);
        VelocityContext context = new VelocityContext();
        context.put("application", applicationInfo);
        context.put("projectInfo", projectInfo);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_APPLICATION_FILE_PATH_FOR_SPRINGBOOT, filePath, context);
        MyLog.info("创建主入口类 Application.java {} 成功！", FileUtil.normalize(filePath));

        String ymlPath = String.format("%s%s/src/main/resources/application.yml", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_APPLICATION_YML_FILE_PATH_FOR_SPRINGBOOT, ymlPath, context);
        MyLog.info("创建配置文件 application.yml {} 成功！", FileUtil.normalize(ymlPath));

        String pomPath = String.format("%s%s/pom.xml", projectRoot, projectInfo.getArtifactId());
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_SPRINGBOOT, pomPath, context);
        MyLog.info("创建配置文件 pom.xml {} 成功！！", FileUtil.normalize(pomPath));

        String testFile = String.format("%s%s/src/test/java/%s%sApiTest.java", projectRoot, artifactId, packagePath, File.separator);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TEST_FILE_PATH_FOR_SPRINGBOOT, testFile, context);
        MyLog.info("创建测试类 ApiTest.java {} 成功！", FileUtil.normalize(testFile));

        String ignoreFile = String.format("%s%s%s%s", projectRoot, artifactId, File.separator, ".gitignore");
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH_FOR_SPRINGBOOT, ignoreFile, context);
        MyLog.info("创建配置文件 .gitignore {} 成功！", FileUtil.normalize(ignoreFile));

        String applicationPath = String.format("%s%s/src/main/java/%s/controller/package-info.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s.controller", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH_FOR_SPRINGBOOT, applicationPath, context);

        String domainPath = String.format("%s%s/src/main/java/%s/entity/package-info.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s.entity", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH_FOR_SPRINGBOOT, domainPath, context);

        String infrastructurePath = String.format("%s%s/src/main/java/%s/service/package-info.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s.service", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH_FOR_SPRINGBOOT, infrastructurePath, context);

        String interfacesPath = String.format("%s%s/src/main/java/%s/service/impl/package-info.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s.service.impl", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH_FOR_SPRINGBOOT, interfacesPath, context);
        MyLog.info("创建DDD分层和描述文件 {} 成功！", "package-info.java");

    }

    private void writeToFile(String templateName, String filePath, VelocityContext context) {
        File file = new File(filePath);
        TemplateEngine.writeToFileByTemplate(templateName, file, context);
    }

    /**
     * 生成包名
     *
     * @return
     */
    private String generatePackageName(ProjectInfo projectInfo) {
        String lastPackageName = projectInfo.getArtifactId()
                .replaceAll("-", "").toLowerCase();
        String packageName = String.format("%s%s%s", projectInfo.getGroupId(), Constant.DOT_PATH, lastPackageName);
        if (!JavaIdentifierUtil.isValidJavaFullClassName(packageName)) {
            String exceptionMsg = String.format("The package name %s is invalid", packageName);
            throw new DefinedException(exceptionMsg);
        }
        return packageName;
    }

    @Override
    public void cliGenerator() {

    }

    @Override
    public void pomGenerator(String projectRoot, ProjectInfo projectInfo) {
        String packageName = generatePackageName(projectInfo);
        String packagePath = packageName.replace(Constant.DOT_PATH, File.separator);
        ApplicationInfo applicationInfo = getApplicationInfo(projectInfo);
        String className = applicationInfo.getClassName();
        String artifactId = projectInfo.getArtifactId();
        VelocityContext context = new VelocityContext();
        context.put("application", applicationInfo);
        context.put("projectInfo", projectInfo);

        String pomPath = String.format("%s%s/pom.xml", projectRoot, projectInfo.getArtifactId());
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_API, pomPath, context);
        MyLog.info("创建配置文件 pom.xml {} 成功！！", FileUtil.normalize(pomPath));

        String ignoreFile = String.format("%s%s/.gitignore", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_API, ignoreFile, context);
        MyLog.info("创建配置文件 .gitignore {} 成功！", FileUtil.normalize(ignoreFile));

        String readmeFile = String.format("%s%s/readme.vm", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_API, readmeFile, context);
        MyLog.info("创建配置文件 ReadMe.md {} 成功！", FileUtil.normalize(readmeFile));

        String httpRunner4jFile = String.format("%s%s/src/main/java/%s/HttpRunner4j.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FILE_PATH_FOR_API, httpRunner4jFile, context);


        String functionFile = String.format("%s%s/src/main/java/%s/functions/JokeFunction.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s.functions", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FILE_PATH_FOR_API, functionFile, context);


        String jokeTestFile = String.format("%s%s/src/test/java/%s/%s/JokeTest.java", projectRoot, artifactId, packagePath, "joke");
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_API, jokeTestFile, context);
        MyLog.info("创建测试类 JokeTest.java {} 成功！", FileUtil.normalize(jokeTestFile));

        String mockTestFile = String.format("%s%s/src/test/java/%s/%s/MockTest.java", projectRoot, artifactId, packagePath, "rap2");
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_API, mockTestFile, context);
        MyLog.info("创建测试类 MockTest.java {} 成功！", FileUtil.normalize(mockTestFile));


    }

    private ApplicationInfo getApplicationInfo(ProjectInfo projectInfo) {
        String[] split = projectInfo.getArtifactId().split("-");
        StringBuffer applicationJavaName = new StringBuffer();
        Arrays.asList(split).forEach(s -> {
            applicationJavaName.append(s.substring(0, 1).toUpperCase() + s.substring(1));
        });
        applicationJavaName.append("Application");
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setPackageName(generatePackageName(projectInfo));
        applicationInfo.setClassName(applicationJavaName.toString());
        return applicationInfo;
    }


}
