package vip.lematech.hrun4j.cli.service;

import vip.lematech.hrun4j.model.scaffolding.ProjectInfo;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
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
     * Hrun4j CLi dependency generation
     *
     * @param projectRoot project root
     * @param projectName project info
     */
    void cliGenerator(String projectRoot, String projectName);
    /**
     * Hrun4j POM dependency generation
     * @param projectRoot project root
     * @param projectInfo project info
     */
    void pomGenerator(String projectRoot, ProjectInfo projectInfo);
}
