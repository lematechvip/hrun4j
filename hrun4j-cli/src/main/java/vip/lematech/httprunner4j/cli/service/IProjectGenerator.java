package vip.lematech.httprunner4j.cli.service;

import vip.lematech.httprunner4j.model.scaffolding.ProjectInfo;

/**
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public interface IProjectGenerator {

    /**
     * Generate Maven springboot project scaffolding
     *
     * @param projectRoot project root
     * @param projectInfo project info
     */
    void springbootGenerator(String projectRoot, ProjectInfo projectInfo);

    /**
     * HttpRunner4J CLi dependency generation
     *
     * @param projectRoot project root
     * @param projectName project info
     */
    void cliGenerator(String projectRoot, String projectName);
    /**
     * HttpRunner4J POM dependency generation
     * @param projectRoot project root
     * @param projectInfo project info
     */
    void pomGenerator(String projectRoot, ProjectInfo projectInfo);
}
