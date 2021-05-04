package io.lematech.httprunner4j.cli.commands;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.URLUtil;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.cli.har.HarUtils;
import io.lematech.httprunner4j.cli.har.model.*;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Har2Yml
 * @description The <code>har2case</code> command.
 * @created 2021/4/18 7:53 下午
 * @publicWechat lematech
 */
public class Har2Case extends Command {
    @Option(name = "--file", usage = "Specify the HAR file path.")
    File harFile;
    @Argument
    String format;

    @Override
    public String description() {
        return "Print har2yml command information.";
    }

    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        FilesUtil.checkFileExists(harFile);
        MyLog.info("Start generating test cases,testcase format:{}", format);
        Har har;
        try {
            har = HarUtils.read(harFile);
        } catch (Exception e) {
            String exceptionMsg = String.format("Error reading HAR file:%s,Exception information:%s", FilesUtil.getCanonicalPath(harFile), e.getMessage());
            MyLog.error(exceptionMsg);
            return 1;
        }
        if (Objects.isNull(har.getLog())) {
            String exceptionMsg = String.format("HAR file %s has no pages!", FilesUtil.getCanonicalPath(harFile));
            MyLog.error(exceptionMsg);
            return 1;
        }

        Config config = new Config();
        config.setName("Testcase descritpion");
        config.setVariables(Maps.newHashMap());
        config.setVerify(false);
        List<TestStep> testSteps = new ArrayList<>();
        List<HarPage> harPages = har.getLog().getPages();
        MyLog.info("Number of pages viewed: " + harPages.size());
        for (HarPage page : harPages) {
            MyLog.info(page.toString());
            MyLog.info("Output the calls for this page: ");
            for (HarEntry entry : page.getEntries()) {
                MyLog.info("\t" + entry);
                TestStep testStep = new TestStep();
                HarRequest request = entry.getRequest();
                testStep.setName(String.format("Request api:%s", URLUtil.getPath(request.getUrl())));
                RequestEntity requestEntity = new RequestEntity();
                requestEntity.setMethod(request.getMethod());
                requestEntity.setUrl(request.getUrl());
                //set headers
                List<HarHeader> harHeaders = request.getHeaders();
                Map<String, Object> headers = Maps.newHashMap();
                for (HarHeader harHeader : harHeaders) {
                    headers.put(String.valueOf(harHeader.getName()), harHeader.getValue());
                }
                requestEntity.setHeaders(headers);
                //set cookie
                List<HarCookie> harCookies = request.getCookies();
                Map<String, Object> cookie = Maps.newHashMap();
                for (HarCookie harCookie : harCookies) {
                    cookie.put(String.valueOf(harCookie.getName()), harCookie.getValue());
                }
                requestEntity.setCookies(cookie);
                List<HarQueryParm> queryParams = request.getQueryString();
                Map<String, Object> params = Maps.newHashMap();
                for (HarQueryParm harQueryParm : queryParams) {
                    params.put(String.valueOf(harQueryParm.getName()), harQueryParm.getValue());
                }
                requestEntity.setParams(params);
                testStep.setRequest(requestEntity);
                testSteps.add(testStep);
            }
        }
        return 0;
    }


}
