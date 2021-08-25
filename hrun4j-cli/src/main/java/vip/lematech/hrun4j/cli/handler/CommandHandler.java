package vip.lematech.hrun4j.cli.handler;

import vip.lematech.hrun4j.cli.commands.AllCommands;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import java.util.AbstractList;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class CommandHandler extends OptionHandler {
    /**
     * This constructor is required by the args4j framework.
     *
     * @param parser Command Parser
     * @param option Option defined
     * @param setter Setter
     */
    public CommandHandler(final CmdLineParser parser, final OptionDef option,
                          final Setter<Object> setter) {
        super(parser,
                new OptionDef(AllCommands.names(), "<command>",
                        option.required(), option.help(), option.hidden(),
                        CommandHandler.class, option.isMultiValued()) {
                }, setter);
    }

    /**
     * @param params  Command line arguments
     * @return Command line execution results
     * @throws CmdLineException command line exception
     */
    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        final String subCmd = params.getParameter(0);

        for (final Command c : AllCommands.get()) {
            if (c.name().equals(subCmd)) {
                parseSubArguments(c, params);
                setter.addValue(c);
                return params.size();
            }
        }
        throw new CmdLineException(owner,
                Messages.ILLEGAL_OPERAND.format(option.toString(), subCmd));
    }

    @Override
    public String getDefaultMetaVariable() {
        return "<command>";
    }

    /**
     * @param c
     * @param params
     * @throws CmdLineException
     */
    private void parseSubArguments(final Command c, final Parameters params)
            throws CmdLineException {
        final CmdLineParser p = new CommandParser(c);
        p.parseArgument(new AbstractList<String>() {
            @Override
            public String get(final int index) {
                try {
                    return params.getParameter(index + 1);
                } catch (final CmdLineException e) {
                    // invalid index was accessed.
                    throw new IndexOutOfBoundsException();
                }
            }

            @Override
            public int size() {
                return params.size() - 1;
            }
        });
    }


}
