package io.lematech.httprunner4j.cli.scaffolding.domain.service.module.impl;

import io.lematech.httprunner4j.cli.scaffolding.domain.model.ProjectInfo;
import io.lematech.httprunner4j.cli.scaffolding.domain.service.module.BaseModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class GenerationIgnore extends BaseModule {

    private Logger logger = LoggerFactory.getLogger(GenerationPom.class);

    public void doGeneration(ProjectInfo projectInfo, String projectsRoot) throws Exception {
        File file = new File(
                projectsRoot + projectInfo.getArtifactId() + "/",
                ".gitignore"
        );

        // 写入文件
        super.writeFile(file, "ignore.ftl", projectInfo);

        logger.info("创建配置文件 .gitignore {}", file.getPath());
    }

}
