package vip.lematech.httprunner4j.cli.commands;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import vip.lematech.httprunner4j.cli.handler.Command;
import vip.lematech.httprunner4j.cli.util.HarUtils;
import vip.lematech.httprunner4j.cli.model.har.Har;
import vip.lematech.httprunner4j.cli.model.har.HarEntry;
import vip.lematech.httprunner4j.cli.model.har.HarPage;
import vip.lematech.httprunner4j.helper.LogHelper;
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
			String exceptionMsg = String.format("Error reading HAR file:%s,Exception information:%s", FileUtil.getAbsolutePath(harFilePath), e.getMessage());
			LogHelper.error(exceptionMsg);
			return 1;
		}

		if (Objects.isNull(har.getLog()) || CollectionUtil.isEmpty(har.getLog().getPages())) {
			String exceptionMsg = String.format("HAR file %s has no pages!", FileUtil.getAbsolutePath(harFilePath));
			LogHelper.error(exceptionMsg);
			return 1;
		}
		HarUtils.connectReferences(har, filterSuffix, filterUriByKeywords);
		List<HarPage> harPages = har.getLog().getPages();
		viewInConsole(harPages);
		return 0;
	}

	private void viewInConsole(List<HarPage> harPages) {
		LogHelper.info("Httprunner4j start displaying: ");
		LogHelper.info("Number of pages viewed: " + harPages.size());
		for (HarPage page : harPages) {
			LogHelper.info(page.toString());
			LogHelper.info("Output the calls for this page: ");
			for (HarEntry entry : page.getEntries()) {
				LogHelper.info("\t" + entry);
			}
		}
	}

}
