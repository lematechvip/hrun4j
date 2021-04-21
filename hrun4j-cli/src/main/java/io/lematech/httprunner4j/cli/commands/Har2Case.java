package io.lematech.httprunner4j.cli.commands;

import io.lematech.httprunner4j.cli.Command;

import java.io.PrintWriter;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Har2Yml
 * @description The <code>har2case</code> command.
 * @created 2021/4/18 7:53 下午
 * @publicWechat lematech
 */
public class Har2Case extends Command {
    @Override
    public String description() {
        return "Print har2yml command information.";
    }

    @Override
    public int execute(PrintWriter out, PrintWriter err) throws Exception {
        return 0;
    }
}
