package vip.lematech.hrun4j.cli.commands;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import vip.lematech.hrun4j.cli.helper.HarHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.model.har.Har;
import vip.lematech.hrun4j.model.har.HarEntry;
import vip.lematech.hrun4j.model.har.HarPage;
import vip.lematech.hrun4j.cli.handler.Command;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;


/**
 * The <code>viewhar</code> command.
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
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
	 * @return Command line execution results
	 */
	@Override
	public int execute(PrintWriter out, PrintWriter err) {
		File harFilePath = new File(this.file);
		Har har;
		try {
			har = HarHelper.read(harFilePath);
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
		HarHelper.connectReferences(har, filterSuffix, filterUriByKeywords);
		List<HarPage> harPages = har.getLog().getPages();
		viewInConsole(harPages);
		return 0;
	}

	private void viewInConsole(List<HarPage> harPages) {
		LogHelper.info("hrun4j start displaying: ");
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
