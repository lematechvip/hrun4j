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
public class GenerationYml extends BaseModule {

    private Logger logger = LoggerFactory.getLogger(GenerationYml.class);

    public void doGeneration(ProjectInfo projectInfo, String projectsRoot) throws Exception {

        File file = new File(
                projectsRoot + projectInfo.getArtifactId() + "/src/main/resources/",
                "application.yml"
        );

        // 写入文件
        super.writeFile(file, "yml.vm", null);
        logger.info("创建配置文件 application.yml {}", file.getPath());
    }

}
