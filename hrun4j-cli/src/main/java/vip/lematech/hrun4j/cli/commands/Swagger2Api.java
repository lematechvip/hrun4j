package vip.lematech.hrun4j.cli.commands;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.CaseFormat;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.SwaggerParser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.Option;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import vip.lematech.hrun4j.cli.constant.CliConstants;
import vip.lematech.hrun4j.cli.handler.Command;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.ApiModel;
import vip.lematech.hrun4j.helper.FilesHelper;
import vip.lematech.hrun4j.helper.JsonHelper;
import vip.lematech.hrun4j.helper.LogHelper;

import java.io.*;
import java.util.*;

/**
 * The <code>swagger2Api</code> command.
 * @author: chenfanghang
 * website https://www.lematech.vip/
 * @version 1.0.1
 */
public class Swagger2Api extends Command {

    @Option(name = "--file", usage = "Specify the HAR file path.", required = true)
    String source;

    @Option(name = "--format", usage = "Generate use case format, support 2y/2j.")
    String format = CliConstants.GENERATE_YML_FORMAT;

    @Option(name = "--api_dir", usage = "Specifies the path  of the generated use case.")
    File generateCaseDirectory;

    @Override
    public String description() {
        return "Print swagger2api command information.";
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
        return parse(source, target);
    }

    public int parse(String pathOrUrl, String generateCaseDirectory) {
        Swagger2Api swagger2Api = new Swagger2Api();
        Yaml yaml = new Yaml(new Constructor(JSONObject.class));
        Swagger swagger;
        if (pathOrUrl.startsWith("http")) {
            try {
                swagger = new SwaggerParser().read(pathOrUrl);
            } catch (Exception e) {
                String exceptionMsg = String.format("Error reading swagger url:%s,Exception information:%s", pathOrUrl, e.getMessage());
                LogHelper.error(exceptionMsg);
                return 1;
            }
        } else {
            try {
                File f1 = new File(pathOrUrl);
                FilesHelper.checkFileExists(f1);
                String content = JSON.toJSONString(yaml.load(new FileInputStream(f1)));
                swagger = new SwaggerParser().readWithInfo(content).getSwagger();
            } catch (FileNotFoundException e) {
                String exceptionMsg = String.format("Error reading swagger file:%s,Exception information:%s", FileUtil.getAbsolutePath(pathOrUrl), e.getMessage());
                LogHelper.error(exceptionMsg);
                return 1;
            }
        }
        String basePath = null;
        if ("2.0".equals(swagger.getInfo().getTitle())) {
            basePath = swagger.getBasePath();
            if (StringUtils.isNotBlank(swagger.getHost())) {
                basePath = "http://" + swagger.getHost() + basePath;
            }
        } else {
            try {
                SwaggerParseResult result;
                if (pathOrUrl.startsWith("http")) {
                    result = new OpenAPIParser().readLocation(pathOrUrl, null, null);
                } else {
                    File f1 = new File(pathOrUrl);
                    FilesHelper.checkFileExists(f1);
                    String content = JSON.toJSONString(yaml.load(new FileInputStream(f1)));
                    result = new OpenAPIParser().readContents(content, null, null);
                }
                if (result.getOpenAPI().getServers() != null && !result.getOpenAPI().getServers().isEmpty()) {
                    basePath = result.getOpenAPI().getServers().get(0).getUrl();
                }
            } catch (FileNotFoundException e) {
                String exceptionMsg = String.format("Error reading swagger file:%s,Exception information:%s", FileUtil.getAbsolutePath(pathOrUrl), e.getMessage());
                LogHelper.error(exceptionMsg);
                return 1;
            }
        }
        Map<String, Path> paths = swagger.getPaths();
        Set<String> pathNames = paths.keySet();
        Map<String, Model> definitions = swagger.getDefinitions();

        String finalBasePath = basePath;
        pathNames.parallelStream().forEach(pathName -> {
            Path path = paths.get(pathName);
            Map<HttpMethod, Operation> operationMap = path.getOperationMap();
            Set<HttpMethod> httpMethods = operationMap.keySet();
            for (HttpMethod method : httpMethods) {
                Operation operation = operationMap.get(method);
                RequestEntity request = new RequestEntity();
                request.setUrl(pathName);
                request.setMethod(method.toString());
                swagger2Api.parseParameters(operation, request, definitions, pathName);
                ApiModel apiModel = buildApiModel(operation, finalBasePath, request);
                String filePath;
                if (operation.getTags() != null && !operation.getTags().isEmpty()) {
                    String subdir = operation.getTags().get(0);
                    filePath = generateCaseDirectory + "/" + subdir;
                    FileUtil.mkdir(filePath);
                } else {
                    filePath = generateCaseDirectory;
                }
                dumpFile(filePath, apiModel);
            }
        });
        return 0;
    }

    private void dumpFile(String path, ApiModel api) {
        try {
            File jsonFile;
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(api, SerializerFeature.PrettyFormat), Feature.OrderedField);
            if (Objects.isNull(format) || format.equalsIgnoreCase(CliConstants.GENERATE_YML_FORMAT)) {
                jsonFile = new File(path, String.format("%s.%s", api.getName(), "yml"));
                File fileParent = jsonFile.getParentFile();
                FileUtil.mkdir(fileParent);
                DumperOptions dumperOptions = new DumperOptions();
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                Yaml y = new Yaml(dumperOptions);
                y.dump(data, new FileWriter(jsonFile));
            } else {
                jsonFile = new File(path, String.format("%s.%s", api.getName(), "json"));
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

    private ApiModel buildApiModel(Operation operation, String basePath, RequestEntity request) {
        String name;
        if (StringUtils.isNotEmpty(operation.getOperationId())) {
            name = operation.getOperationId();
        } else {
            name = request.getUrl();
        }
        name = parseName(name);
        ApiModel apiModel = new ApiModel();
        apiModel.setName(name);
        apiModel.setBaseUrl(basePath);
        apiModel.setRequest(request);
        return apiModel;
    }

    private String parseName(String name) {
        if (StrUtil.startWith(name, "/")) {
            name = StrUtil.sub(name, 1, name.length());
        }
        name = name.replace("/", "_");
        String name2 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name2);
    }

    private void parseParameters(Operation operation, RequestEntity request, Map<String, Model> definitions, String pathName) {

        List<Parameter> parameters = operation.getParameters();

        for (Parameter parameter : parameters) {
            switch (parameter.getIn()) {
                case "path":
                    parsePathParameters(parameter, pathName, request);
                    break;
                case "header":
                    if (request.getHeaders() == null) {
                        request.setHeaders(new HashMap<>());
                    }
                    parseHeaderParameters(parameter, request.getHeaders());
                    break;
                case "query":
                    if (request.getParams() == null) {
                        request.setParams(new HashMap<>());
                    }
                    parseQueryParameters(parameter, request.getParams());
                    break;
                case "formData":
                    if (request.getData() == null) {
                        request.setData(JSONObject.toJSON(new Object()));
                    }
                    if (request.getHeaders() == null) {
                        request.setHeaders(new HashMap<>());
                    }
                    parseFormDataParameters(parameter, request.getData(), request.getHeaders());
                    break;
                case "body":
                    if (request.getJson() == null) {
                        request.setJson(new JSONObject());
                    }
                    request.setJson(parseRequestBodyParameters(parameter, definitions));
                    break;
                case "cookie":
                    if (request.getCookies() == null) {
                        request.setCookies(new HashMap<>());
                    }
                    parseCookieParameters(parameter, request.getCookies());
                    break;
            }
        }
    }

    private void parsePathParameters(Parameter parameter, String url, RequestEntity request) {
        PathParameter pathParameter = (PathParameter) parameter;
        String target = "{" + pathParameter.getName() + "}";
        request.setUrl(url.replace(target, parseValue(pathParameter.getName())));
    }

    private void parseHeaderParameters(Parameter parameter, Map<String, Object> headers) {
        HeaderParameter headerParameter = (HeaderParameter) parameter;
        headers.put(headerParameter.getName(), headerParameter.getDefaultValue() == null ? "" : headerParameter.getDefaultValue());
    }

    private void parseFormDataParameters(Parameter parameter, Object form, Map<String, Object> headers) {
        FormParameter parameters = (FormParameter) parameter;
        JSONObject f = (JSONObject) form;
        if (StringUtils.equals(parameters.getType(), "file")) {
            headers.put("Content-Type", "multipart/form-data");
        } else {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        }
        f.put(parameters.getName(), parseValue(parameters.getName()));
    }

    private void parseQueryParameters(Parameter parameter, Map<String, Object> query) {
        QueryParameter queryParameter = (QueryParameter) parameter;
        query.put(queryParameter.getName(), parseValue(queryParameter.getName()));

    }

    private Object parseRequestBodyParameters(Parameter parameter, Map<String, Model> definitions) {
        BodyParameter bodyParameter = (BodyParameter) parameter;
        Model schema = bodyParameter.getSchema();
        String body = parseSchema(schema, definitions);
        try {
            return JSONObject.parseObject(body);
        } catch (Exception e) {
            return JSONObject.parseArray(body);
        }

    }

    private void parseCookieParameters(Parameter parameter, Map<String, Object> cookies) {
        CookieParameter cookieParameter = (CookieParameter) parameter;
        cookies.put(cookieParameter.getName(), cookieParameter.getDefaultValue() == null ? "" : cookieParameter.getDefaultValue());
    }


    private String parseSchema(Model schema, Map<String, Model> definitions) {
        // 引用模型
        if (schema instanceof RefModel) {
            String simpleRef = "";
            RefModel refModel = (RefModel) schema;
            String originalRef = refModel.getOriginalRef();
            if (refModel.getOriginalRef().split("/").length > 3) {
                simpleRef = originalRef.replace("#/definitions/", "");
            } else {
                simpleRef = refModel.getSimpleRef();
            }
            Model model = definitions.get(simpleRef);
            HashSet<String> refSet = new HashSet<>();
            refSet.add(simpleRef);
            if (model != null) {
                JSONObject bodyParameters = getBodyParameters(model.getProperties(), refSet, definitions);
                return bodyParameters.toJSONString();
            }
        } else if (schema instanceof ArrayModel) {
            //模型数组
            ArrayModel arrayModel = (ArrayModel) schema;
            Property items = arrayModel.getItems();
            JSONArray propertyList = new JSONArray();
            if (items instanceof RefProperty) {
                RefProperty refProperty = (RefProperty) items;
                String simpleRef = refProperty.getSimpleRef();
                HashSet<String> refSet = new HashSet<>();
                refSet.add(simpleRef);
                Model model = definitions.get(simpleRef);
                if (model != null) {
                    propertyList.add(getBodyParameters(model.getProperties(), refSet, definitions));
                } else {
                    propertyList.add(new JSONObject());
                }
            }
            return propertyList.toString();
        } else if (schema instanceof ModelImpl) {
            ModelImpl model = (ModelImpl) schema;
            Map<String, Property> properties = model.getProperties();
            if (properties != null) {
                JSONObject bodyParameters = getBodyParameters(properties, new HashSet<>(), definitions);
                return bodyParameters.toJSONString();
            }
        }
        return "";
    }

    private JSONObject getBodyParameters(Map<String, Property> properties, HashSet<String> refSet, Map<String, Model> definitions) {
        JSONObject jsonObject = new JSONObject();
        if (properties != null) {
            properties.forEach((key, value) -> {
                if (value instanceof ObjectProperty) {
                    ObjectProperty objectProperty = (ObjectProperty) value;
                    jsonObject.put(key, getBodyParameters(objectProperty.getProperties(), refSet, definitions));
                } else if (value instanceof ArrayProperty) {
                    ArrayProperty arrayProperty = (ArrayProperty) value;
                    Property items = arrayProperty.getItems();
                    if (items instanceof RefProperty) {
                        RefProperty refProperty = (RefProperty) items;
                        String simpleRef = refProperty.getSimpleRef();
                        if (refSet.contains(simpleRef)) {
                            //避免嵌套死循环
                            jsonObject.put(key, new JSONArray());
                            return;
                        }
                        refSet.add(simpleRef);
                        Model model = definitions.get(simpleRef);
                        JSONArray propertyList = new JSONArray();
                        if (model != null) {
                            propertyList.add(getBodyParameters(model.getProperties(), refSet, definitions));
                        } else {
                            propertyList.add(new JSONObject());
                        }
                        jsonObject.put(key, propertyList);
                    } else {
                        jsonObject.put(key, new ArrayList<>());
                    }
                } else if (value instanceof RefProperty) {
                    RefProperty refProperty = (RefProperty) value;
                    String simpleRef = refProperty.getSimpleRef();
                    if (refSet.contains(simpleRef)) {
                        //避免嵌套死循环
                        jsonObject.put(key, new JSONArray());
                        return;
                    }
                    refSet.add(simpleRef);
                    Model model = definitions.get(simpleRef);
                    if (model != null) {
                        jsonObject.put(key, getBodyParameters(model.getProperties(), refSet, definitions));
                    }
                } else {
                    jsonObject.put(key, parseValue(key));
                }
            });
        }
        return jsonObject;
    }

    private StringBuffer parseValue(String value) {
        StringBuffer sb = new StringBuffer();
        sb.append("${").append(value).append("}");
        return sb;
    }
}
