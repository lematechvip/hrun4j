package io.lematech.httprunner4j.cli.commands;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import io.lematech.httprunner4j.cli.CliConstants;
import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.cli.har.HarUtils;
import io.lematech.httprunner4j.cli.har.model.Har;
import io.lematech.httprunner4j.cli.har.model.HarEntry;
import io.lematech.httprunner4j.cli.har.model.HarPage;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.kohsuke.args4j.Option;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Version
 * @description The <code>viewhar</code> command.
 * @created 2021/4/18 5:40 下午
 * @publicWechat lematech
 */

public class ViewHar extends Command {

	@Option(name = "--file", usage = "Specify the HAR file path.")
	String file;

	@Option(name = "--filter_suffix", usage = "Filter out the specified request suffix, support multiple suffix formats, multiple in English status ';' division.")
	String filterSuffix;

	@Option(name = "--filter_uri", usage = "Filter out the URIs that meet the requirements by keyword, multiple in English status ';' division.")
	String filterUriByKeywords;

	@Override
	public String description() {
		return "View the HAR file information.";
	}

	/**
	 * View the HAR file information
	 *
	 * @param out std out
	 * @param err std err
	 * @return
	 */
	@Override
	public int execute(PrintWriter out, PrintWriter err) {
		File harFilePath = new File(this.file);
		Har har;
		try {
			har = HarUtils.read(harFilePath);
		} catch (Exception e) {
			String exceptionMsg = String.format("Error reading HAR file:%s,Exception information:%s", FilesUtil.getCanonicalPath(harFilePath), e.getMessage());
			MyLog.error(exceptionMsg);
			return 1;
		}

		if (Objects.isNull(har.getLog()) || CollectionUtil.isEmpty(har.getLog().getPages())) {
			String exceptionMsg = String.format("HAR file %s has no pages!", FilesUtil.getCanonicalPath(harFilePath));
			MyLog.error(exceptionMsg);
			return 1;
		}
		HarUtils.connectReferences(har, filterSuffix, filterUriByKeywords);
		List<HarPage> harPages = har.getLog().getPages();
		viewInConsole(harPages);
		return 0;
	}

	private void viewInConsole(List<HarPage> harPages) {
		MyLog.info("Httprunner4j start displaying: ");
		MyLog.info("Number of pages viewed: " + harPages.size());
		for (HarPage page : harPages) {
			MyLog.info(page.toString());
			MyLog.info("Output the calls for this page: ");
			for (HarEntry entry : page.getEntries()) {
				MyLog.info("\t" + entry);
			}
		}
	}

}
