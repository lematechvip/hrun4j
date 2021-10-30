package vip.lematech.hrun4j.cli.commands;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.Option;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import vip.lematech.hrun4j.cli.constant.CliConstants;
import vip.lematech.hrun4j.cli.handler.Command;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.Config;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import vip.lematech.hrun4j.entity.testcase.TestStep;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.JsonHelper;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.helper.RegExpHelper;
import vip.lematech.hrun4j.model.postman.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static vip.lematech.hrun4j.helper.RegExpHelper.find;

/**
 * The <code>postman2Case</code> command.
 * @author: chenfanghang
 * website https://www.lematech.vip/
 * @version 1.0.1
 */
public class Postman2Case extends Command {

    @Option(name = "--file", usage = "Specify the Postman file path.", required = true)
    File postmanFile;

    @Option(name = "--format", usage = "Generate use case format, support 2y/2j.")
    String format = CliConstants.GENERATE_YML_FORMAT;

    @Option(name = "--case_dir", usage = "Specifies the path  of the generated use case.")
    File generateCaseDirectory;

    @Override
    public String description() {
        return "Print postman2case command information.";
    }

    @Override
    public int execute(PrintWriter out, PrintWriter err) {
        if (!CliConstants.GENERATE_JSON_FORMAT.equalsIgnoreCase(format) && !CliConstants.GENERATE_YML_FORMAT.equalsIgnoreCase(format)) {
            String exceptionMsg = String.format("Specifies the generation format %s exception. Only - 2Y or - 2J format is supported,default set 2y", format);
            LogHelper.warn(exceptionMsg);
        }
        LogHelper.info("Start generating test cases,testcase format:{}", Objects.isNull(format) ? CliConstants.GENERATE_YML_FORMAT : format);

        String target;
        if (Objects.isNull(generateCaseDirectory)) {
            target = FileUtil.getAbsolutePath(new File(Constant.DOT_PATH));
        } else {
            if (!generateCaseDirectory.exists() || !generateCaseDirectory.isDirectory()) {
                String exceptionMsg = String.format("The case directory %s does not exist"
                        , FileUtil.getAbsolutePath(generateCaseDirectory));
                LogHelper.error(exceptionMsg);
                return 1;
            }
            target = FileUtil.getAbsolutePath(generateCaseDirectory);
        }
        return parse(postmanFile, target);
    }

    public int parse(File postmanFile, String generateCaseDirector) {
        FilesHelper.checkFileExists(postmanFile);
        String content;
        try {
            Reader reader = new InputStreamReader(new FileInputStream(postmanFile), StandardCharsets.UTF_8);
            content = parseVariable(IoUtil.read(reader));
        } catch (FileNotFoundException e) {
            String exceptionMsg = String.format("Error reading postman file:%s,Exception information:%s", FileUtil.getAbsolutePath(postmanFile), e.getMessage());
            LogHelper.error(exceptionMsg);
            return 1;
        }
        PostmanCollection postmanCollection = JSON.parseObject(content, PostmanCollection.class);
        List<PostmanKeyValue> variables = postmanCollection.getVariable();
        //生成postmanFile根目录
        String rootDir = generateCaseDirector + "/" + postmanCollection.getInfo().getName();
        FileUtil.mkdir(rootDir);
        parseItem(postmanCollection.getItem(), variables, rootDir);
        return 0;
    }

    private void parseItem(List<PostmanItem> items, List<PostmanKeyValue> variables, String dir) {
        items.parallelStream().forEach(item -> {
            String itemContent = JSON.toJSONString(item);
            String subdirectory;
            List<PostmanItem> childItems = item.getItem();
            if (Objects.nonNull(childItems)) {
                //生成postmanFile子目录
                subdirectory = dir + "/" + item.getName();
                FileUtil.mkdir(subdirectory);
                parseItem(childItems, variables, subdirectory);
            } else {
                PostmanRequest request = item.getRequest();
                TestCase testCase = new TestCase();
                Config config = new Config();
                config.setName(item.getName());
                if (StringUtils.isNotBlank(request.getUrl().getRaw())) {
                    String protocol = request.getUrl().getProtocol();
                    String host = Optional.of(String.join(".", request.getUrl().getHost())).orElse("");
                    String port = StrUtil.isNotEmpty(request.getUrl().getPort()) ? ":" + request.getUrl().getPort() : "";
                    String baseUrl = protocol + "://" + host + port;
                    config.setBaseUrl(baseUrl);
                    config.setVerify(false);
                    List<String> matchList = find(Constant.REGEX_EXTRACT_EXPRESSION, itemContent).stream().distinct().collect(Collectors.toList());
                    if (!matchList.isEmpty()) {
                        Map<String, String> vars = Maps.newHashMap();
                        matchList.forEach(match -> vars.put(match, ""));
                        config.setVariables(vars);
                    }
                }
                List<TestStep> testSteps = new ArrayList<>();
                testSteps.add(buildTestStep(item.getName(), request));
                testCase.setConfig(config);
                testCase.setTestSteps(testSteps);
                subdirectory = dir;
                dumpFile(subdirectory, testCase, item.getName());
            }
        });
    }

    private TestStep buildTestStep(String name, PostmanRequest request) {
        TestStep testStep = new TestStep();
        String testStepName = StringUtils.isNotBlank(request.getDescription()) ? name + " " + request.getDescription() : name;
        testStep.setName(testStepName);
        testStep.setRequest(buildRequestEntity(request));
        List<Map<String, Object>> validates = new ArrayList<>();
        validates.add(buildValidateMap("status_code", 200));
        testStep.setValidate(validates);
        return testStep;
    }

    private RequestEntity buildRequestEntity(PostmanRequest request) {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setMethod(request.getMethod());

        if (StrUtil.isNotEmpty(request.getUrl().getRaw())) {
            String url = "/" + String.join("/", request.getUrl().getPath());
            requestEntity.setUrl(url);
        } else {
            requestEntity.setUrl("");
        }

        Map<String, Object> headers = new HashMap<>();
        if (Objects.nonNull(request.getUrl().getQuery())) {
            Map<String, Object> params = new HashMap<>();
            request.getUrl().getQuery().forEach(query -> {
                params.put(query.getKey(), query.getValue());
            });
            requestEntity.setParams(params);
        }
        parseBody(request.getBody(), headers, requestEntity);
        if (Objects.nonNull(request.getHeader())) {
            request.getHeader().forEach(header -> headers.put(header.getKey(), header.getValue()));
        }
        if (!headers.isEmpty()) {
            requestEntity.setHeaders(headers);
        }
        return requestEntity;
    }

    private Map<String, Object> buildValidateMap(String expect, Object actual) {
        List validateMeta = new ArrayList();
        validateMeta.add(expect);
        validateMeta.add(actual);
        Map<String, Object> validateMap = Maps.newHashMap();
        validateMap.put("eq", validateMeta);
        return validateMap;
    }

    private void parseBody(JSONObject postmanBody, Map<String, Object> headers, RequestEntity requestEntity) {
        if (Objects.isNull(postmanBody)) {
            return;
        }
        String bodyMode = postmanBody.getString("mode");
        if (StringUtils.isBlank(bodyMode)) {
            return;
        }
        if (StringUtils.equals(bodyMode, PostmanRequestBodyModeEnum.RAW.value())) {
            JSONObject options = postmanBody.getJSONObject("options");
            if (options != null) {
                JSONObject raw = options.getJSONObject(PostmanRequestBodyModeEnum.RAW.value());
                if (raw != null) {
                    switch (raw.getString("language").toLowerCase()) {
                        // todo case完善
                        case "text":
                            break;
                        case "javascript":
                            break;
                        case "json":
                            headers.put(CliConstants.GENERATE_MODE_EASY_CONTENT_TYPE_META, CliConstants.APPLICATION_JSON_MIME_TYPE);
                            Object json;
                            try {
                                json = JSONObject.parseObject(postmanBody.getString(bodyMode));
                            } catch (Exception e) {
                                json = JSONObject.parseArray(postmanBody.getString(bodyMode));
                            }
                            requestEntity.setJson(json);
                            break;
                        case "html":
                            break;
                        case "xml":
                            break;

                    }
                }
            } else {
                // 如果options为空默认为json
                headers.put(CliConstants.GENERATE_MODE_EASY_CONTENT_TYPE_META, CliConstants.APPLICATION_JSON_MIME_TYPE);
                Object json;
                try {
                    json = JSONObject.parseObject(postmanBody.getString(bodyMode));
                } catch (Exception e) {
                    json = JSONObject.parseArray(postmanBody.getString(bodyMode));
                }
                requestEntity.setJson(json);
            }
        } else if (StringUtils.equals(bodyMode, PostmanRequestBodyModeEnum.FORM_DATA.value()) || StringUtils.equals(bodyMode, PostmanRequestBodyModeEnum.URLENCODED.value())) {
            List<PostmanKeyValue> postmanKV = JSON.parseArray(postmanBody.getString(bodyMode), PostmanKeyValue.class);
            if (!postmanKV.isEmpty()) {
                if (StringUtils.equals(bodyMode, PostmanRequestBodyModeEnum.FORM_DATA.value())) {
                    headers.put(CliConstants.GENERATE_MODE_EASY_CONTENT_TYPE_META, CliConstants.MULTIPART_FORM_DATA);
                } else if (StringUtils.equals(bodyMode, PostmanRequestBodyModeEnum.URLENCODED.value())) {
                    headers.put(CliConstants.GENERATE_MODE_EASY_CONTENT_TYPE_META, CliConstants.APPLICATION_X_WWW_FORM_URLENCODED);
                }
                JSONObject f = new JSONObject();
                postmanKV.forEach(postmanKeyValue -> f.put(postmanKeyValue.getKey(), postmanKeyValue.getValue()));
                requestEntity.setData(f);
            }
        }
    }

    private void dumpFile(String path, TestCase testCase, String name) {
        try {
            File jsonFile;
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(testCase, SerializerFeature.PrettyFormat), Feature.OrderedField);
            if (Objects.isNull(format) || format.equalsIgnoreCase(CliConstants.GENERATE_YML_FORMAT)) {
                jsonFile = new File(path, String.format("%s.%s", name, "yml"));
                File fileParent = jsonFile.getParentFile();
                FileUtil.mkdir(fileParent);
                DumperOptions dumperOptions = new DumperOptions();
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                Yaml y = new Yaml(dumperOptions);
                y.dump(data, new FileWriter(jsonFile));
            } else {
                jsonFile = new File(path, String.format("%s.%s", name, "json"));
                File fileParent = jsonFile.getParentFile();
                FileUtil.mkdir(fileParent);
                JsonHelper.jsonWriteToFile(jsonFile, data);
            }
            LogHelper.info("Generated successfully! File path:{}", FileUtil.getAbsolutePath(jsonFile));
        } catch (IOException e) {
            e.printStackTrace();
            String exceptionMsg = String.format("Exception occurs when generating test cases. Exception information: %s", e.getMessage());
            LogHelper.error(exceptionMsg);
        }
    }

    private String parseVariable(String variable) {
        List<String> matchList = RegExpHelper.find(Constant.REGEX_POSTMAN_REPLACE_EXPRESSION, variable);
        if (!matchList.isEmpty()) {
            for (String match : matchList) {
                String replaceWord = match.replace("{{", "${").replace("}}", "}");
                String after = variable.replace(match, replaceWord);
                return parseVariable(after);
            }
        }
        return variable;
    }
}
