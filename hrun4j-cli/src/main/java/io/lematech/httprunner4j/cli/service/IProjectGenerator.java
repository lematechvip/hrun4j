package io.lematech.httprunner4j.cli.service;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public interface IProjectGenerator {

    /**
     * Generate Maven springboot project scaffolding
     */
    void springbootGenerator();

    /**
     * HttpRunner4J CLi dependency generation
     */

    void cliGenerator();

    /**
     * HttpRunner4J POM dependency generation
     */
    void pomGenerator();
}
