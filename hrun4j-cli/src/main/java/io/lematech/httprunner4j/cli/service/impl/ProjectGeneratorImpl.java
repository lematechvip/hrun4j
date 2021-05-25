package io.lematech.httprunner4j.cli.service.impl;

import io.lematech.httprunner4j.cli.model.scaffolding.ApplicationInfo;
import io.lematech.httprunner4j.cli.model.scaffolding.ProjectInfo;
import io.lematech.httprunner4j.cli.service.IProjectGenerator;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.core.engine.TemplateEngine;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.Arrays;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public class ProjectGeneratorImpl implements IProjectGenerator {
    private String projectRoot;
    private ProjectInfo projectInfo;

    public ProjectGeneratorImpl(String projectRoot, ProjectInfo projectInfo) {
        this.projectRoot = projectRoot;
        this.projectInfo = projectInfo;
    }

    ;

    @Override
    public void springbootGenerator() {
        String lastPackageName = projectInfo.getArtifactId().replaceAll("-", "").toLowerCase();
        generationApplication(lastPackageName);
        generateYml();
        generatePom();
        generateTest(lastPackageName);
        generateIgnore(lastPackageName);
        generatePackageInfo(lastPackageName);
    }

    @Override
    public void cliGenerator() {

    }

    @Override
    public void pomGenerator() {

    }

    /**
     * @param lastPackageName
     */
    private void generatePackageInfo(String lastPackageName) {
        String packageName = String.format("%s%s%s", projectInfo.getGroupId(), Constant.DOT_PATH, lastPackageName);
        String packagePath = packageName.replace(Constant.DOT_PATH, File.separator);
        VelocityContext context = new VelocityContext();
        ApplicationInfo applicationInfo = new ApplicationInfo();
        context.put("application", applicationInfo);
        String applicationPath = String.format("%s%s/src/main/java/%s/controller/package-info.java", this.projectRoot, this.projectInfo.getArtifactId(), packagePath);
        applicationInfo.setPackageName(String.format("%s.controller", packageName));
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH, new File(applicationPath), context);
        String domainPath = String.format("%s%s/src/main/java/%s/entity/package-info.java", this.projectRoot, this.projectInfo.getArtifactId(), packagePath);
        applicationInfo.setPackageName(String.format("%s.entity", packageName));
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH, new File(domainPath), context);
        String infrastructurePath = String.format("%s%s/src/main/java/%s/service/package-info.java", this.projectRoot, this.projectInfo.getArtifactId(), packagePath);
        applicationInfo.setPackageName(String.format("%s.service", packageName));
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH, new File(infrastructurePath), context);
        String interfacesPath = String.format("%s%s/src/main/java/%s/service/impl/package-info.java", this.projectRoot, this.projectInfo.getArtifactId(), packagePath);
        applicationInfo.setPackageName(String.format("%s.service.impl", packageName));
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH, new File(interfacesPath), context);
        MyLog.info("创建DDD分层和描述文件 {} 成功！", "package-info.java");
    }

    /**
     * Create the entry class
     *
     * @param lastPackageName
     */
    private void generationApplication(String lastPackageName) {
        ApplicationInfo applicationInfo = getApplicationInfo(lastPackageName);
        String packagePath = applicationInfo.getPackageName().replace(Constant.DOT_PATH, File.separator) + File.separator;
        String filePath = String.format("%s%s/src/main/java/%s%s.java", this.projectRoot, this.projectInfo.getArtifactId(), packagePath, applicationInfo.getClassName());
        File file = new File(filePath);
        VelocityContext context = new VelocityContext();
        context.put("application", applicationInfo);
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_APPLICATION_FILE_PATH, file, context);
        MyLog.info("创建主入口类 Application.java {} 成功！", file.getPath());
    }

    /**
     *
     */
    public void generateYml() {
        String ymlPath = String.format("%s%s/src/main/resources/application.yml", this.projectRoot, this.projectInfo.getArtifactId());
        File file = new File(ymlPath);
        VelocityContext context = new VelocityContext();
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_APPLICATION_YML_FILE_PATH, file, context);
        MyLog.info("创建配置文件 application.yml {} 成功！", file.getPath());
    }

    /**
     *
     */
    public void generatePom() {
        String pomPath = String.format("%s%s/pom.xml", this.projectRoot, this.projectInfo.getArtifactId());
        File file = new File(pomPath);
        VelocityContext context = new VelocityContext();
        context.put("projectInfo", projectInfo);
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_POM_FILE_PATH, file, context);
        MyLog.info("创建配置文件 pom.xml {} 成功！", file.getPath());
    }

    /**
     * @param lastPackageName
     */
    public void generateTest(String lastPackageName) {
        ApplicationInfo applicationInfo = getApplicationInfo(lastPackageName);
        String packagePath = applicationInfo.getPackageName().replace(Constant.DOT_PATH, File.separator) + File.separator;
        String filePath = String.format("%s%s/src/test/java/%sApiTest.java", this.projectRoot, this.projectInfo.getArtifactId(), packagePath);
        File file = new File(filePath);
        VelocityContext context = new VelocityContext();
        context.put("application", applicationInfo);
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_TEST_FILE_PATH, file, context);
        MyLog.info("创建测试类 ApiTest.java {} 成功！", file.getPath());

    }


    /**
     * @param lastPackageName
     */
    public void generateIgnore(String lastPackageName) {
        ApplicationInfo applicationInfo = getApplicationInfo(lastPackageName);
        String filePath = String.format("%s%s%s.gitignore", this.projectRoot, this.projectInfo.getArtifactId(), File.separator, ".gitignore");
        File file = new File(filePath);
        VelocityContext context = new VelocityContext();
        context.put("application", applicationInfo);
        TemplateEngine.writeToFileByTemplate(Constant.SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH, file, context);
        MyLog.info("创建配置文件 .gitignore {} 成功！", file.getPath());

    }

    /**
     * @param lastPackageName
     * @return
     */
    private ApplicationInfo getApplicationInfo(String lastPackageName) {
        String[] split = projectInfo.getArtifactId().split("-");
        StringBuffer applicationJavaName = new StringBuffer();
        Arrays.asList(split).forEach(s -> {
            applicationJavaName.append(s.substring(0, 1).toUpperCase() + s.substring(1));
        });
        applicationJavaName.append("Application");
        String fullPackageName = String.format("%s.%s", this.projectInfo.getGroupId(), lastPackageName);
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setPackageName(fullPackageName);
        applicationInfo.setClassName(applicationJavaName.toString());
        return applicationInfo;
    }

}
