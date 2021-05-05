package io.lematech.httprunner4j.cli.commands;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.cli.Command;
import io.lematech.httprunner4j.cli.Constants;
import io.lematech.httprunner4j.cli.har.HarUtils;
import io.lematech.httprunner4j.cli.har.model.*;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.testcase.Config;
import io.lematech.httprunner4j.entity.testcase.TestCase;
import io.lematech.httprunner4j.entity.testcase.TestStep;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import io.lematech.httprunner4j.widget.utils.JsonUtil;
import org.kohsuke.args4j.Option;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    @Option(name = "--file", usage = "Specify the HAR file path.", required = true)
    File harFile;

    @Option(name = "--format", usage = "Generate use case format, support -2y/-2j.")
    String format = Constants.GENERATE_YML_FORMAT;

    @Option(name = "--case_dir", usage = "Specifies the path  of the generated use case.")
    File generateCaseDirectory;

    @Option(name = "generationMode", usage = "Specified generation mode, full mode / easy mode.")
    String generationMode = "easyMode";

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
        HarUtils.connectReferences(har);
        TestCase testCase = new TestCase();
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
                List<HarHeader> harRequestHeaders = request.getHeaders();
                Map<String, Object> requestHeaders = Maps.newHashMap();
                for (HarHeader harHeader : harRequestHeaders) {
                    requestHeaders.put(String.valueOf(harHeader.getName()), harHeader.getValue());
                }
                requestEntity.setHeaders(requestHeaders);
                //set cookie
                List<HarCookie> harRequestCookies = request.getCookies();
                Map<String, Object> requestCookie = Maps.newHashMap();
                for (HarCookie harCookie : harRequestCookies) {
                    requestCookie.put(String.valueOf(harCookie.getName()), harCookie.getValue());
                }
                requestEntity.setCookies(requestCookie);
                //set get  params
                List<HarQueryParm> queryParams = request.getQueryString();
                Map<String, Object> params = Maps.newHashMap();
                if (queryParams.size() > 0) {
                    for (HarQueryParm harQueryParm : queryParams) {
                        params.put(String.valueOf(harQueryParm.getName()), harQueryParm.getValue());
                    }
                }
                //set post params
                HarPostData harPostData = request.getPostData();
                if (!Objects.isNull(harPostData)) {
                    String postContent = harPostData.getText();
                    if (JsonUtil.isJson(postContent)) {
                        requestEntity.setJson(JSONObject.parseObject(postContent));
                    }
                    List<HarPostParam> postParams = harPostData.getParams();
                    Map<String, Object> postParam = Maps.newHashMap();
                    if (Objects.nonNull(postParams) && postParams.size() > 0) {
                        for (HarPostParam harPostParam : postParams) {
                            postParam.put(String.valueOf(harPostParam.getName()), harPostParam.getValue());
                        }
                    }
                }

                //set response headers
                List<Map<String, Object>> validate = new ArrayList<>();
                HarResponse response = entry.getResponse();
                int statusCode = response.getStatus();
                validate.add(buildValidateMap("statusCode", statusCode));
                List<HarHeader> headers = response.getHeaders();
                for (HarHeader harHeader : headers) {
                    String headerName = harHeader.getName();
                    String headerValue = harHeader.getValue();
                    String headerJmesPath = String.format("%s%s%s", Constant.DATA_EXTRACTOR_JMESPATH_HEADERS_START, Constant.DOT_PATH, headerName);
                    validate.add(buildValidateMap(headerJmesPath, headerValue));
                }
                testStep.setValidate(validate);
                testStep.setRequest(requestEntity);
                testSteps.add(testStep);
            }
            testCase.setConfig(config);
            testCase.setTestSteps(testSteps);

            try {
                String workDirPath;
                String caseFileName = FileUtil.getName(harFile);
                if (Objects.isNull(generateCaseDirectory)) {
                    workDirPath = FilesUtil.getCanonicalPath(new File(Constant.DOT_PATH));
                } else {
                    if (!generateCaseDirectory.exists() || !generateCaseDirectory.isDirectory()) {
                        String exceptionMsg = String.format("The case directory %s does not exist"
                                , FilesUtil.getCanonicalPath(generateCaseDirectory));
                        MyLog.error(exceptionMsg);
                        return 1;
                    }
                    workDirPath = FilesUtil.getCanonicalPath(generateCaseDirectory);
                }
                new Yaml().dump(testCase, new FileWriter(new File(workDirPath, String.format("%s.%s", caseFileName, "yml"))));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                String exceptionMsg = String.format("\n" +
                        "生成测试用例出现异常，异常信息:\n" +
                        "Exception occurs when generating test cases. Exception information: %s", ioException.getMessage());
                MyLog.error(exceptionMsg);
                return 1;
            }
        }
        return 0;
    }

    private Map<String, Object> buildValidateMap(String expect, Object actual) {
        List validateMeta = new ArrayList();
        validateMeta.add(expect);
        validateMeta.add(actual);
        Map<String, Object> validateMap = Maps.newHashMap();
        validateMap.put("eq", validateMeta);
        return validateMap;
    }

}
