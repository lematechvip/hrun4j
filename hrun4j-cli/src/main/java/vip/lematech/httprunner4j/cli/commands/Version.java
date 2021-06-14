package vip.lematech.httprunner4j.cli.commands;

import vip.lematech.httprunner4j.cli.handler.Command;

import java.io.PrintWriter;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Version
 * @description The <code>version</code> command.
 * @created 2021/4/18 5:40 下午
 * @publicWechat lematech
 */
public class Version extends Command {
    public static final String VERSION = "1.0.0";
    @Override
    public String description() {
        return "Print httprunner4j version information.";
    }

    /**
     * Print httprunner4j version information.
     *
     * @param out std out
     * @param err std err
     * @return
     */
    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        out.println(VERSION);
        return 0;
    }
}
