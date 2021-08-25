package vip.lematech.hrun4j.cli.commands;

import vip.lematech.hrun4j.cli.handler.Command;

import java.util.Arrays;
import java.util.List;

/**
 * List of all available commands.
 *
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
public final class AllCommands {
    private AllCommands() {
    }

    /**
     * @return list of new instances of all available commands
     */
    public static List<Command> get() {
        return Arrays.asList(new Version()
                , new Run(), new ViewHar(), new Har2Case(), new Swagger2Api(), new Postman2Case()
                , new StartProject());
    }

    /**
     * @return String containing all available command names
     */
    public static String names() {
        final StringBuilder sb = new StringBuilder();
        for (final Command c : get()) {
            if (sb.length() > 0) {
                sb.append('|');
            }
            sb.append(c.name());
        }
        return sb.toString();
    }
}
