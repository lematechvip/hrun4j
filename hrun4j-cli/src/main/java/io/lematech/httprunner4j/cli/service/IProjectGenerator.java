package io.lematech.httprunner4j.cli.service;

import io.lematech.httprunner4j.cli.model.scaffolding.ProjectInfo;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public interface IProjectGenerator {

    /**
     * Generate Maven springboot project scaffolding
     *
     * @param projectRoot
     * @param projectInfo
     */
    void springbootGenerator(String projectRoot, ProjectInfo projectInfo);

    /**
     * HttpRunner4J CLi dependency generation
     */

    void cliGenerator();

    /**
     * HttpRunner4J POM dependency generation
     */
    void pomGenerator();
}
