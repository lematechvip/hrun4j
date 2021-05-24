package io.lematech.httprunner4j.cli.scaffolding.domain.service.module.impl;

import io.lematech.httprunner4j.cli.scaffolding.domain.model.ApplicationInfo;
import io.lematech.httprunner4j.cli.scaffolding.domain.model.ProjectInfo;
import io.lematech.httprunner4j.cli.scaffolding.domain.service.module.BaseModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class GenerationTest extends BaseModule {

    private Logger logger = LoggerFactory.getLogger(GenerationTest.class);

    public void doGeneration(ProjectInfo projectInfo, String projectsRoot, String lastPackageName, StringBuffer applicationJavaName) throws Exception {
        ApplicationInfo applicationInfo = new ApplicationInfo(
                projectInfo.getGroupId() + "." + lastPackageName,
                applicationJavaName.toString()
        );

        String packagePath = applicationInfo.getPackageName().replace(".", "/") + "/";

        File file = new File(projectsRoot + projectInfo.getArtifactId() + "/src/test/java/" + packagePath,
                "ApiTest.java");

        // 写入文件
        super.writeFile(file, "test.vm", applicationInfo);

        logger.info("创建测试类 ApiTest.java {}", file.getPath());
    }

}
