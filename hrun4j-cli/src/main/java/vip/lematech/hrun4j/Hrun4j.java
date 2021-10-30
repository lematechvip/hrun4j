package vip.lematech.hrun4j;

import vip.lematech.hrun4j.cli.handler.Command;
import vip.lematech.hrun4j.cli.handler.CommandHandler;
import vip.lematech.hrun4j.cli.handler.CommandParser;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Entry point for all command line operations.
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class Hrun4j extends Command {

    private final String[] args;
    private static final PrintWriter NUL = new PrintWriter(new Writer() {
        @Override
        public void write(final char[] arg0, final int arg1, final int arg2)
                 {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    });

    @Argument(handler = CommandHandler.class, required = true)
    Command command;

    Hrun4j(final String... args) {
        this.args = args;
    }

    @Override
    public String description() {
        return "Command line interface for hrun4j.";
    }

    @Override
    public String usage(final CommandParser parser) {
        return JAVACMD + "--help | <command>";
    }

    @Override
    public int execute(PrintWriter out, PrintWriter err) throws Exception {

        final CommandParser mainParser = new CommandParser(this);
        try {
            mainParser.parseArgument(args);
        } catch (final CmdLineException e) {
            ((CommandParser) e.getParser()).getCommand().printHelp(err);
            err.println();
            err.println(e.getMessage());
            return -1;
        }
        if (help) {
            printHelp(out);
            return 0;
        }
        if (command.help) {
            command.printHelp(out);
            return 0;
        }
        if (command.quiet) {
            out = NUL;
        }
        return command.execute(out, err);
    }

    /**
     * Main entry point for program invocations.
     *
     * @param args program arguments
     * @throws Exception All internal exceptions are directly passed on to get printed
     *                   on the console
     */
    public static void main(final String... args) throws Exception {
        final PrintWriter out = new PrintWriter(System.out, true);
        final PrintWriter err = new PrintWriter(System.err, true);
        final int returnCode = new Hrun4j(args).execute(out, err);
        System.exit(returnCode);
    }
}
