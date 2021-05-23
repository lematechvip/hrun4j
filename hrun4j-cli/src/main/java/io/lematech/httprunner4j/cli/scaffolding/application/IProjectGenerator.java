package io.lematech.httprunner4j.cli.scaffolding.application;


import io.lematech.httprunner4j.cli.scaffolding.domain.model.ProjectInfo;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public interface IProjectGenerator {

    void generator(ProjectInfo projectInfo) throws Exception;

}
