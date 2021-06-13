package vip.lematech.httprunner4j.cli.service;

import vip.lematech.httprunner4j.model.scaffolding.ProjectInfo;

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
     *
     * @param projectRoot
     * @param projectName
     */
    void cliGenerator(String projectRoot, String projectName);

    /**
     * HttpRunner4J POM dependency generation
     */
    void pomGenerator(String projectRoot, ProjectInfo projectInfo);
}
