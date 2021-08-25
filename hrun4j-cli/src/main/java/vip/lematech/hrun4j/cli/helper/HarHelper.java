/**
 * har - HAR file reader, writer and viewer
 * Copyright (c) 2014-2016, Sandeep Gupta
 * <p>
 * http://sangupta.com/projects/har
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.lematech.hrun4j.cli.helper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonSyntaxException;
import com.sangupta.jerry.util.UriUtils;
import vip.lematech.hrun4j.model.har.Har;
import vip.lematech.hrun4j.model.har.HarEntry;
import vip.lematech.hrun4j.model.har.HarLog;
import vip.lematech.hrun4j.model.har.HarPage;
import vip.lematech.hrun4j.cli.constant.CliConstants;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.helper.FilesHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for working with HAR files.
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class HarHelper {

	/**
	 * Read the HAR file and create an {@link Har} model instance from the same.
	 *
	 * @param file
	 *            the file to be read
	 *
	 * @return the {@link Har} instance
	 *
	 * @throws JsonSyntaxException
	 *             if the JSON is not well formed
	 *
	 * @throws IOException
	 *             if reading the file fails
	 *
	 * @throws DefinedException
	 *             if the file does not exist, is a directory or is not a valid
	 *             file
	 */
	public static Har read(File file) throws JsonSyntaxException, IOException {
		FilesHelper.checkFileExists(file);
		return GsonHepler.getGson().fromJson(FileUtils.readFileToString(file, Constant.CHARSET_UTF_8), Har.class);
	}


	/**
	 * Connect references between page and entries so that they can be obtained as needed.
	 * @param har
	 * @param filterSuffix
	 * @param filterUriByKeywords
	 */
	public static void connectReferences(Har har, String filterSuffix, String filterUriByKeywords) {
		List<String> filterSuffixList = new ArrayList<>();
		if (Objects.nonNull(filterSuffix)) {
			filterSuffixList = ListUtil.toList(filterSuffix.split(CliConstants.FILTER_REQUEST_SUFFIX_SEPARATOR));
		}

		List<String> filterUriList = new ArrayList<>();
		if (Objects.nonNull(filterUriByKeywords)) {
			filterUriList = ListUtil.toList(filterUriByKeywords.split(CliConstants.FILTER_REQUEST_SUFFIX_SEPARATOR));
		}

		if (Objects.isNull(har)) {
			throw new DefinedException("HAR object cannot be null");
		}
		HarLog harLog = har.getLog();
		List<HarPage> harPages = har.getLog().getPages();
		List<HarEntry> harEntries = har.getLog().getEntries();
		if (Objects.isNull(har.getLog())) {
			LogHelper.warn("HAR file invalid");
			return;
		}

		boolean isRestfulApi = false;
		if (CollectionUtil.isEmpty(harPages)) {
			if (CollectionUtil.isEmpty(harEntries)) {
				LogHelper.warn("No page found");
				return;
			}
			isRestfulApi = true;
			harPages = new ArrayList<>();
			HarEntry harEntry = harEntries.get(0);
			HarPage harPage = new HarPage();
			harPage.setEntries(harEntries);
			harPage.setComment("For RESTful API requests, Hrun4j is automatically added");
			harPage.setId(StrUtil.isEmpty(harEntry.getPageref()) ? harEntry.getRequest().getUrl() : harEntry.getPageref());
			harPage.setStartedDateTime(harEntry.getStartedDateTime());
			harPage.setTitle("For RESTful API requests");
			harPages.add(harPage);
			harLog.setPages(harPages);
		}

		if (CollectionUtil.isEmpty(harEntries)) {
			LogHelper.warn("No har entry - initialize empty list");
			for (HarPage page : har.getLog().getPages()) {
				page.setEntries(new ArrayList<>());
			}
			return;
		}

		for (HarPage page : harPages) {
			String pageID = page.getId();
			List<HarEntry> entries = new ArrayList<>();
			for (HarEntry entry : harEntries) {
				if (isRestfulApi || harPages.size() == 1) {
					addRequestEntry(filterSuffixList, filterUriList, entries, entry);
				} else {
					if (pageID.equals(StrUtil.isEmpty(entry.getPageref()) ? entry.getRequest().getUrl() : entry.getPageref())) {
						addRequestEntry(filterSuffixList, filterUriList, entries, entry);
					}
				}
			}
			Collections.sort(entries);
			page.setEntries(entries);
		}
	}

	/**
	 * Add request entry information
	 *
	 * @param filterSuffixList
	 * @param filterUriList
	 * @param entries
	 * @param entry
	 */
	private static void addRequestEntry(List<String> filterSuffixList, List<String> filterUriList, List<HarEntry> entries, HarEntry entry) {
		String baseUri = UriUtils.extractPath(entry.getRequest().getUrl());
		String requestSuffix = FileUtil.extName(baseUri);
		if (filterSuffixList.size() > 0 || filterUriList.size() > 0) {
			if (filterSuffixList.contains(requestSuffix) || filterUriList.contains(baseUri)) {
				entries.add(entry);
			}
			return;
		}
		entries.add(entry);
	}


}
