package vip.lematech.hrun4j.cli.service.impl;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.cli.constant.CliConstants;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.model.scaffolding.ApplicationInfo;
import vip.lematech.hrun4j.model.scaffolding.ProjectInfo;
import vip.lematech.hrun4j.cli.testsuite.TemplateEngine;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.cli.service.IProjectGenerator;
import org.apache.velocity.VelocityContext;

import java.io.*;
import java.util.Arrays;
import java.util.Map;


/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
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

        String ymlPath = String.format("%s%s/src/main/resources/application.yml", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_APPLICATION_YML_FILE_PATH_FOR_SPRINGBOOT, ymlPath, context);

        String pomPath = String.format("%s%s/pom.xml", projectRoot, projectInfo.getArtifactId());
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_SPRINGBOOT, pomPath, context);

        String testFile = String.format("%s%s/src/test/java/%s%sApiTest.java", projectRoot, artifactId, packagePath, File.separator);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TEST_FILE_PATH_FOR_SPRINGBOOT, testFile, context);

        String ignoreFile = String.format("%s%s%s%s", projectRoot, artifactId, File.separator, ".gitignore");
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH_FOR_SPRINGBOOT, ignoreFile, context);

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
        LogHelper.info("创建  {} 文件成功！", FileUtil.normalize(file.getAbsolutePath()));
    }

    private void writeToFile(String templateName, String filePath) {
        File file = new File(filePath);
        TemplateEngine.writeToFileByTemplate(templateName, file, new VelocityContext());
        LogHelper.info("创建  {} 文件成功！", FileUtil.normalize(file.getAbsolutePath()));
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
    public void cliGenerator(String projectRoot, String artifactId) {
        Map<String,String> templateToFileMap = Maps.newHashMap();
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_APIS_GET_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/apis/get.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_FORM_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/apis/postFormData.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_RAW_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/apis/postRawText.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_BSH_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/bsh/test.bsh"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_DATA_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/data/csvFile.csv"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_GET_GETSCENE_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/testcases/get/getScene.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_POST_POSTSCENE_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/testcases/post/postScene.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_HR_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/Hrun4j.bsh"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_ENV_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/.env"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_IGNORE_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/.gitignore"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_README_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/readMe.md"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_TESTSUITE_FILE_PATH_FOR_CLI
                ,String.format("%s%s%s", projectRoot, artifactId,"/testsuite/testsuite.yml"));

        for(Map.Entry<String,String> entry : templateToFileMap.entrySet()){
            String template = entry.getKey();
            String writePath = entry.getValue();
            writeToFile(template,writePath);
        }
        String projectPath = String.format("%s%s", projectRoot, artifactId);
        LogHelper.info("脚手架工程初始化成功！ 工程路径：{}",projectPath);

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
        String testSuiteFile = String.format("%s%s/src/test/resources/testsuite/testsuite.xml", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TESTSUITE_FILE_PATH_FOR_API, testSuiteFile, context);
        String testSuiteJokeFile = String.format("%s%s/src/test/resources/testsuite/testsuite_all.xml", projectRoot, artifactId);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_TESTSUITE_ALL_FILE_PATH_FOR_API, testSuiteJokeFile, context);
        String hrun4jFile = String.format("%s%s/src/main/java/%s/Hrun4j.java", projectRoot, artifactId, packagePath);
        applicationInfo.setPackageName(String.format("%s", packageName));
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_HRUN4J_FILE_PATH_FOR_API, hrun4jFile, context);
        String functionFile = String.format("%s%s/src/main/java/%s/functions/MyFunction.java", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_HRUN4J_FUNCTION_FILE_PATH_FOR_API, functionFile, context);
        String getTestFile = String.format("%s%s/src/test/java/%s/testcases/get/GetTest.java", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_JOKE_TEST_FILE_PATH_FOR_API, getTestFile, context);
        String postTestFile = String.format("%s%s/src/test/java/%s/testcases/post/PostTest.java", projectRoot, artifactId, packagePath);
        writeToFile(CliConstants.SCAFFOLD_TEMPLATE_RAP2_TEST_FILE_PATH_FOR_API, postTestFile, context);
        Map<String,String> templateToFileMap = Maps.newHashMap();
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_APIS_GET_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/apis/get.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_FORM_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/apis/postFormData.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_RAW_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/apis/postRawText.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_DATA_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/data/csvFile.csv"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_GET_GETSCENE_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/testcases/get/getScene.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_POST_POSTSCENE_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/testcases/post/postScene.yml"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_HR_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/Hrun4j.bsh"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_ENV_FILE_PATH_FOR_API
                ,String.format("%s%s/src/test/resources/%s", projectRoot, artifactId,"/.env"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_IGNORE_FILE_PATH_FOR_API
                ,String.format("%s%s%s", projectRoot, artifactId,"/.gitignore"));
        templateToFileMap.put(CliConstants.SCAFFOLD_TEMPLATE_RESOURCES_README_FILE_PATH_FOR_API
                ,String.format("%s%s%s", projectRoot, artifactId,"/readMe.md"));
        for(Map.Entry<String,String> entry : templateToFileMap.entrySet()){
            String template = entry.getKey();
            String writePath = entry.getValue();
            writeToFile(template,writePath);
        }
        String projectPath = String.format("%s%s", projectRoot, artifactId);
        LogHelper.info("脚手架工程初始化成功！ 工程路径：{}",projectPath);

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
