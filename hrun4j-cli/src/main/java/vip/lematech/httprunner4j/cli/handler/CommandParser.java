package vip.lematech.httprunner4j.cli.handler;

import org.kohsuke.args4j.CmdLineParser;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className CammandParser
 * @description Parser which remembers the parsed command to have additional context
 * information to produce help output.
 * @created 2021/4/18 2:50 下午
 * @publicWechat lematech
 */
public class CommandParser extends CmdLineParser {
    private final Command command;

    /**
     * init command object
     *
     * @param command
     */
    public CommandParser(final Command command) {
        super(command);
        this.command = command;
    }

    /**
     * get command object
     *
     * @return
     */
    public Command getCommand() {
        return command;
    }
}
