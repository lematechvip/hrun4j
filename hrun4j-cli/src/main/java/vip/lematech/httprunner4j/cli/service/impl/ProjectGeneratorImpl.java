package vip.lematech.httprunner4j.cli.service.impl;

import cn.hutool.core.io.FileUtil;
import vip.lematech.httprunner4j.cli.constant.CliConstants;
import vip.lematech.httprunner4j.cli.testsuite.TemplateEngine;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.common.DefinedException;
import vip.lematech.httprunner4j.helper.JavaIdentifierHelper;
import vip.lematech.httprunner4j.helper.LogHelper;
import vip.lematech.httprunner4j.model.scaffolding.ApplicationInfo;
import vip.lematech.httprunner4j.model.scaffolding.ProjectInfo;
import vip.lematech.httprunner4j.cli.service.IProjectGenerator;
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
        LogHelper.info("创建主入口类 Application.java {} 成功！", FileUtil.normalize(filePath));

        String ymlPath = String.format("%s%s/src/main/resources/application.yml", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_APPLICATION_YML_FILE_PATH_FOR_SPRINGBOOT, ymlPath, context);
        LogHelper.info("创建配置文件 application.yml {} 成功！", FileUtil.normalize(ymlPath));

        String pomPath = String.format("%s%s/pom.xml", projectRoot, projectInfo.getArtifactId());
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_SPRINGBOOT, pomPath, context);
        LogHelper.info("创建配置文件 pom.xml {} 成功！！", FileUtil.normalize(pomPath));

        String testFile = String.format("%s%s/src/test/java/%s%sApiTest.java", projectRoot, artifactId, packagePath, File.separator);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TEST_FILE_PATH_FOR_SPRINGBOOT, testFile, context);
        LogHelper.info("创建测试类 ApiTest.java {} 成功！", FileUtil.normalize(testFile));

        String ignoreFile = String.format("%s%s%s%s", projectRoot, artifactId, File.separator, ".gitignore");
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH_FOR_SPRINGBOOT, ignoreFile, context);
        LogHelper.info("创建配置文件 .gitignore {} 成功！", FileUtil.normalize(ignoreFile));

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
        LogHelper.info("创建DDD分层和描述文件 {} 成功！", "package-info.java");

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
        if (!JavaIdentifierHelper.isValidJavaFullClassName(packageName)) {
            String exceptionMsg = String.format("The package name %s is invalid", packageName);
            throw new DefinedException(exceptionMsg);
        }
        return packageName;
    }

    @Override
    public void cliGenerator(String projectRoot, String projectName) {
        File sourceFile = new File(this.getClass().getClassLoader().getResource("vm/scaffold/httprunner4j/cli").getFile());
        String targetPath = new File(projectRoot,projectName).getAbsolutePath();
        File targetFile = FileUtil.mkdir(targetPath);
        FileUtil.copyContent(sourceFile,targetFile,true);
        LogHelper.info("脚手架工程初始化成功！ 工程路径：{}",targetFile.getAbsolutePath());
    }

    @Override
    public void pomGenerator(String projectRoot, ProjectInfo projectInfo) {
        String packageName = generatePackageName(projectInfo);
        String packagePath = packageName.replace(Constant.DOT_PATH, File.separator);
        ApplicationInfo applicationInfo = getApplicationInfo(projectInfo);
        String artifactId = projectInfo.getArtifactId();
        VelocityContext context = new VelocityContext();
        context.put("application", applicationInfo);
        context.put("projectInfo", projectInfo);
        String pomPath = String.format("%s%s/pom.xml", projectRoot, projectInfo.getArtifactId());
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_API, pomPath, context);
        LogHelper.info("创建配置文件 pom.xml {} 成功！！", FileUtil.normalize(pomPath));
        String testSuiteFile = String.format("%s%s/src/test/resources/testsuite/testsuite.xml", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TESTSUITE_FILE_PATH_FOR_API, testSuiteFile, context);
        LogHelper.info("创建测试集testsuite.xml {} 成功！", FileUtil.normalize(testSuiteFile));
        String testSuiteJokeFile = String.format("%s%s/src/test/resources/testsuite/testsuite_all.xml", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TESTSUITE_ALL_FILE_PATH_FOR_API, testSuiteJokeFile, context);
        LogHelper.info("创建测试集testsuite_all.xml {} 成功！", FileUtil.normalize(testSuiteJokeFile));
        String httpRunner4jFile = String.format("%s%s/src/main/java/%s/HttpRunner4j.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FILE_PATH_FOR_API, httpRunner4jFile, context);
        String functionFile = String.format("%s%s/src/main/java/%s/functions/MyFunction.java", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FUNCTION_FILE_PATH_FOR_API, functionFile, context);
        String getTestFile = String.format("%s%s/src/test/java/%s/testcases/get/GetTest.java", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_JOKE_TEST_FILE_PATH_FOR_API, getTestFile, context);
        LogHelper.info("创建测试类 GetTest.vm {} 成功！", FileUtil.normalize(getTestFile));
        String postTestFile = String.format("%s%s/src/test/java/%s/testcases/post/PostTest.java", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_RAP2_TEST_FILE_PATH_FOR_API, postTestFile, context);
        LogHelper.info("创建测试类 PostTest.vm {} 成功！", FileUtil.normalize(postTestFile));
        String resourcePath = "vm/scaffold/httprunner4j/pom/resources";
        String targetResourcePath = String.format("%s%s/src/test/resources/", projectRoot, artifactId);
        File targetFile = fileCopy(resourcePath, targetResourcePath);
        String metaInfoPath = "vm/scaffold/httprunner4j/pom/meta-info";
        String rootPath = String.format("%s%s", projectRoot, artifactId);
        File metaFile = fileCopy(metaInfoPath, rootPath);
        LogHelper.info("初始化.gitignore、readMe.md成功！ 文件路径：{}",metaFile.getAbsolutePath());
        LogHelper.info("脚手架工程初始化成功！ 工程路径：{}",targetFile.getAbsolutePath());
    }

    private File fileCopy(String sourcePath,String targetPath) {
        File sourceFile = new File(this.getClass().getClassLoader().getResource(sourcePath).getFile());
        File targetFile = FileUtil.mkdir(new File(targetPath).getAbsolutePath());
        FileUtil.copyContent(sourceFile,targetFile,true);
        return targetFile;
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
