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

package io.lematech.httprunner4j.cli.har;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import io.lematech.httprunner4j.cli.har.model.Har;
import io.lematech.httprunner4j.cli.har.model.HarEntry;
import io.lematech.httprunner4j.cli.har.model.HarPage;
import io.lematech.httprunner4j.cli.har.util.GsonUtils;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author sangupta/lematech@foxmail.com
 * @version 1.0.0
 * @className Har
 * @description Utility class for working with HAR files.
 * @created 2021/5/2 8:35 下午
 * @publicWechat lematech
 */

public class HarUtils {

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
		FilesUtil.checkFileExists(file);
		return GsonUtils.getGson().fromJson(FileUtils.readFileToString(file, Constant.CHARSET_UTF_8), Har.class);
	}


	/**
	 * Read the harJson  and create an {@link Har} model instance from the same.
	 * @param harJson
	 * @return
	 * @throws JsonSyntaxException
	 */
	public static Har read(String harJson) throws JsonSyntaxException {
		if (StrUtil.isEmpty(harJson)) {
			throw new DefinedException("HAR Json cannot be null/empty");
		}
		return GsonUtils.getGson().fromJson(harJson, Har.class);
	}

	public static Har read(Reader harReader) throws JsonSyntaxException {
		if (Objects.isNull(harReader)) {
			throw new DefinedException("HAR reader cannot be null");
		}
		return GsonUtils.getGson().fromJson(harReader, Har.class);
	}

	public static Har read(JsonElement jsonElement) throws JsonSyntaxException {
		if (Objects.isNull(jsonElement)) {
			throw new DefinedException("HAR JsonElement cannot be null");
		}
		return GsonUtils.getGson().fromJson(jsonElement, Har.class);
	}

	/**
	 * Connect references between page and entries so that they can be obtained as needed.
	 *
	 * @param har
	 */
	public static void connectReferences(Har har) {
		if (Objects.isNull(har)) {
			throw new DefinedException("HAR object cannot be null");
		}
		List<HarPage> harPages = har.getLog().getPages();
		List<HarEntry> harEntries = har.getLog().getEntries();
		if (Objects.isNull(har.getLog()) || CollectionUtil.isEmpty(harPages)) {
			MyLog.warn("No page found");
			return;
		}
		if (CollectionUtil.isEmpty(harEntries)) {
			MyLog.warn("No har entry - initialize empty list");
			for (HarPage page : har.getLog().getPages()) {
				page.setEntries(new ArrayList<>());
			}

			return;
		}
		for (HarPage page : harPages) {
			String pageID = page.getId();
			List<HarEntry> entries = new ArrayList<>();
			for (HarEntry entry : harEntries) {
				if (pageID.equals(entry.getPageref())) {
					entries.add(entry);
				}
			}
			Collections.sort(entries);
			page.setEntries(entries);
		}
	}


}
