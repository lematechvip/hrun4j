package vip.lematech.hrun4j.cli.commands;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.sangupta.jerry.util.UriUtils;
import vip.lematech.hrun4j.cli.constant.CliConstants;
import vip.lematech.hrun4j.cli.helper.HarHelper;
import vip.lematech.hrun4j.cli.handler.Command;
import vip.lematech.hrun4j.model.har.*;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.Config;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import vip.lematech.hrun4j.entity.testcase.TestStep;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.JavaIdentifierHelper;
import vip.lematech.hrun4j.helper.JsonHelper;
import org.kohsuke.args4j.Option;
import org.yaml.snakeyaml.DumperOptions;
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
 * The <code>har2case</code> command.
 *
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
public class Har2Case extends Command {

    @Option(name = "--file", usage = "Specify the HAR file path.", required = true)
    File harFile;

    @Option(name = "--format", usage = "Generate use case format, support 2y/2j.")
    String format = CliConstants.GENERATE_YML_FORMAT;

    @Option(name = "--case_dir", usage = "Specifies the path  of the generated use case.")
    File generateCaseDirectory;

    @Option(name = "--gen_mode", usage = "Specified generation mode, full/easy mode.")
    String generationMode = CliConstants.GENERATE_MODE_EASY;

    @Option(name = "--filter_suffix", usage = "Filter out the specified request suffix, support multiple suffix formats, multiple in English status ';' division.")
    String filterSuffix;

    @Option(name = "--filter_uri", usage = "Filter out the URIs that meet the requirements by keyword, multiple in English status ';' division.")
    String filterUriByKeywords;

    @Override
    public String description() {
        return "Print har2yml command information.";
    }

    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        FilesHelper.checkFileExists(harFile);
        if (!CliConstants.GENERATE_JSON_FORMAT.equalsIgnoreCase(format) && !CliConstants.GENERATE_YML_FORMAT.equalsIgnoreCase(format)) {
            String exceptionMsg = String.format("Specifies the generation format %s exception. Only - 2Y or - 2J format is supported,default set 2y", format);
            LogHelper.warn(exceptionMsg);
        }
        LogHelper.info("Start generating test cases,testcase format:{}", Objects.isNull(format) ? CliConstants.GENERATE_YML_FORMAT : format);
        Har har;
        try {
            har = HarHelper.read(harFile);
        } catch (Exception e) {
            String exceptionMsg = String.format("Error reading HAR file:%s,Exception information:%s", FileUtil.getAbsolutePath(harFile), e.getMessage());
            LogHelper.error(exceptionMsg);
            return 1;
        }
        if (Objects.isNull(har.getLog())) {
            String exceptionMsg = String.format("HAR file %s has no pages!", FileUtil.getAbsolutePath(harFile));
            LogHelper.error(exceptionMsg);
            return 1;
        }
        String workDirPath;
        if (Objects.isNull(generateCaseDirectory)) {
            workDirPath = FileUtil.getAbsolutePath(new File(Constant.DOT_PATH));
        } else {
            if (!generateCaseDirectory.exists() || !generateCaseDirectory.isDirectory()) {
                String exceptionMsg = String.format("The case directory %s does not exist"
                        , FileUtil.getAbsolutePath(generateCaseDirectory));
                LogHelper.error(exceptionMsg);
                return 1;
            }
            workDirPath = FileUtil.getAbsolutePath(generateCaseDirectory);
        }
        HarHelper.connectReferences(har, filterSuffix, filterUriByKeywords);
        TestCase testCase = new TestCase();
        Config config = new Config();
        config.setName("Testcase Description");
        config.setVariables(Maps.newHashMap());
        config.setVerify(false);
        List<TestStep> testSteps = new ArrayList<>();
        List<HarPage> harPages = har.getLog().getPages();
        LogHelper.info("Number of pages viewed: " + harPages.size());
        for (int index = 0; index < harPages.size(); index++) {
            HarPage page = harPages.get(index);
            LogHelper.info(page.toString());
            LogHelper.info("Output the calls for this page: ");
            for (HarEntry entry : page.getEntries()) {
                LogHelper.info("\t" + entry);
                testSteps.add(buildTestStep(entry));
            }
            testCase.setConfig(config);
            testCase.setTestSteps(testSteps);
            try {
                String caseFileName = JavaIdentifierHelper.formatFilePath(FileUtil.mainName(harFile));
                if (harPages.size() > 1) {
                    caseFileName = String.format("%s_%s", caseFileName, index + 1);
                }
                File jsonFile;
                JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(testCase, SerializerFeature.PrettyFormat), Feature.OrderedField);
                if (Objects.isNull(format) || format.equalsIgnoreCase(CliConstants.GENERATE_YML_FORMAT)) {
                    jsonFile = new File(workDirPath, String.format("%s.%s", caseFileName, "yml"));
                    DumperOptions dumperOptions = new DumperOptions();
                    dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                    Yaml yaml = new Yaml(dumperOptions);
                    yaml.dump(data, new FileWriter(jsonFile));
                } else {
                    jsonFile = new File(workDirPath, String.format("%s.%s", caseFileName, "json"));
                    JsonHelper.jsonWriteToFile(jsonFile, data);
                }
                LogHelper.info("Generated successfully! File path:{}", FileUtil.getAbsolutePath(jsonFile));
                return 0;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                String exceptionMsg = String.format("Exception occurs when generating test cases. Exception information: %s", ioException.getMessage());
                LogHelper.error(exceptionMsg);
                return 1;
            }
        }
        return 0;
    }

    private TestStep buildTestStep(HarEntry entry) {
        HarRequest request = entry.getRequest();
        String requestUrl = request.getUrl();
        String url = String.format("%s%s", UriUtils.getBaseUrl(requestUrl), UriUtils.extractPath(requestUrl));
        TestStep testStep = new TestStep();
        testStep.setName(String.format("Request api:%s", UriUtils.extractPath(requestUrl)));
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setMethod(request.getMethod());
        requestEntity.setUrl(url);
        //set headers
        List<HarHeader> harRequestHeaders = request.getHeaders();
        Map<String, String> requestHeaders = Maps.newHashMap();
        for (HarHeader harHeader : harRequestHeaders) {
            String headerName = harHeader.getName();
            if (headerName.startsWith(CliConstants.PSEUDO_REQUEST_HEADER)) {
                continue;
            }
            if (!CliConstants.HAR_REQUEST_HEADER_COOKIE.equalsIgnoreCase(harHeader.getName())) {
                requestHeaders.put(harHeader.getName(), harHeader.getValue());
            }
        }
        requestEntity.setHeaders(requestHeaders);
        //set cookie
        List<HarCookie> harRequestCookies = request.getCookies();
        Map<String, Object> requestCookie = Maps.newHashMap();
        for (HarCookie harCookie : harRequestCookies) {
            requestCookie.put(String.valueOf(harCookie.getName()), harCookie.getValue());
        }
        if (MapUtil.isNotEmpty(requestCookie)) {
            requestEntity.setCookies(requestCookie);
        }
        //set get  params
        List<HarQueryParm> queryParams = request.getQueryString();
        Map<String, Object> params = Maps.newHashMap();
        if (queryParams.size() > 0) {
            for (HarQueryParm harQueryParm : queryParams) {
                params.put(String.valueOf(harQueryParm.getName()), harQueryParm.getValue());
            }
            requestEntity.setParams(params);
        }
        //set post params
        HarPostData harPostData = request.getPostData();
        if (!Objects.isNull(harPostData)) {
            String postContent = harPostData.getText();
            String mimeType = harPostData.getMimeType();
            if (CliConstants.APPLICATION_JSON_MIME_TYPE.equalsIgnoreCase(mimeType) || CliConstants.APPLICATION_JSON_MIME_TYPE_UTF_8.equalsIgnoreCase(mimeType)) {
                if (JsonHelper.isJson(postContent)) {
                    requestEntity.setJson(JSONObject.parseObject(postContent));
                } else {
                    LogHelper.warn("Data type is {}, but data value :{} cannot be JSON formatted", CliConstants.APPLICATION_JSON_MIME_TYPE_UTF_8, postContent);
                }
            } else {
                requestEntity.setData(postContent);
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
        validate.add(buildValidateMap("status_code", statusCode));
        List<HarHeader> headers = response.getHeaders();
        for (HarHeader harHeader : headers) {
            String headerName = harHeader.getName();
            String headerValue = harHeader.getValue();
            String headerJmesPath = String.format("%s%s%s", Constant.DATA_EXTRACTOR_JMESPATH_HEADERS_START, Constant.DOT_PATH, headerName);
            if (CliConstants.GENERATE_MODE_EASY.equalsIgnoreCase(generationMode)) {
                if (CliConstants.GENERATE_MODE_EASY_CONTENT_TYPE_META.equalsIgnoreCase(headerName) && false) {
                    validate.add(buildValidateMap(headerJmesPath, headerValue));
                }
            } else if (CliConstants.GENERATE_MODE_FULL.equalsIgnoreCase(generationMode)) {
                validate.add(buildValidateMap(headerJmesPath, headerValue));
            }
        }
        buildInJsonContentValidateItem(validate, response);
        testStep.setValidate(validate);
        testStep.setRequest(requestEntity);
        return testStep;
    }

    /**
     * @param validate
     * @param response
     */
    private void buildInJsonContentValidateItem(List<Map<String, Object>> validate, HarResponse response) {
        HarContent harContent = response.getContent();
        String mimeType = harContent.getMimeType();
        if (CliConstants.APPLICATION_JSON_MIME_TYPE.equalsIgnoreCase(mimeType) || CliConstants.APPLICATION_JSON_MIME_TYPE_UTF_8.equalsIgnoreCase(mimeType)) {
            String content = harContent.getText();
            String encoding = harContent.getEncoding();
            if (CliConstants.APPLICATION_ENCODING_BASE64.equalsIgnoreCase(encoding)) {
                content = Base64.decodeStr(content);
            }
            if (JsonHelper.isJson(content)) {
                JSONObject jsonContent = JSONObject.parseObject(content);
                List<String> defaultItems = new ArrayList<>();
                defaultItems.add("code");
                defaultItems.add("msg");
                for (String item : defaultItems) {
                    if (jsonContent.containsKey(item)) {
                        Object codeValue = jsonContent.get(item);
                        validate.add(buildValidateMap(String.format("body.%s", item), codeValue));
                    }
                }
            } else {
                LogHelper.warn("MIME type returned is application/json, but response content {} cannot be JSON", content);
            }
        }
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
