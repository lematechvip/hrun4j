package io.lematech.httprunner4j.cli.scaffolding.domain.service;

import io.lematech.httprunner4j.cli.scaffolding.application.IProjectGenerator;
import io.lematech.httprunner4j.cli.scaffolding.domain.model.ProjectInfo;
import io.lematech.httprunner4j.cli.scaffolding.domain.service.module.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

public class ProjectGeneratorImpl implements IProjectGenerator {

    private Logger logger = LoggerFactory.getLogger(ProjectGeneratorImpl.class);

    private GenerationApplication generationApplication = new GenerationApplication();
    private GenerationYml generationYml = new GenerationYml();
    private GenerationPom generationPom = new GenerationPom();
    private GenerationTest generationTest = new GenerationTest();
    private GenerationIgnore generationIgnore = new GenerationIgnore();
    private GenerationPackageInfo generationPackageInfo = new GenerationPackageInfo();

    @Override
    public void generator(ProjectInfo projectInfo) throws Exception {

        URL resource = this.getClass().getResource("/");
        String projectsRoot = resource.getFile() + "/projects/";

        String lastPackageName = projectInfo.getArtifactId().replaceAll("-", "").toLowerCase();
        //启动类名称
        String[] split = projectInfo.getArtifactId().split("-");
        StringBuffer applicationJavaName = new StringBuffer();
        Arrays.asList(split).forEach(s -> {
            applicationJavaName.append(s.substring(0, 1).toUpperCase() + s.substring(1));
        });
        applicationJavaName.append("Application");

        // 1. 创建  Application.java
        generationApplication.doGeneration(projectInfo, projectsRoot, lastPackageName, applicationJavaName);

        // 2. 生成 application.yml
        generationYml.doGeneration(projectInfo, projectsRoot);

        // 3. 生成 pom.xml
        generationPom.doGeneration(projectInfo, projectsRoot);

        // 4. 创建测试类 ApiTest.java
        generationTest.doGeneration(projectInfo, projectsRoot, lastPackageName, applicationJavaName);

        // 5. 生成 .gitignore
        generationIgnore.doGeneration(projectInfo, projectsRoot);

        // 6. DDD 四层描述文件
        generationPackageInfo.doGeneration(projectInfo, projectsRoot, lastPackageName, applicationJavaName);

    }

}
