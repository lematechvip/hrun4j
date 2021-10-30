package vip.lematech.hrun4j.cli.handler;

import org.kohsuke.args4j.CmdLineParser;

/**
 * Parser which remembers the parsed command to have additional context
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class CommandParser extends CmdLineParser {
    private final Command command;

    /**
     * init command object
     *
     * @param command command
     */
    public CommandParser(final Command command) {
        super(command);
        this.command = command;
    }

    /**
     * get command object
     *
     * @return Command object
     */
    public Command getCommand() {
        return command;
    }
}
