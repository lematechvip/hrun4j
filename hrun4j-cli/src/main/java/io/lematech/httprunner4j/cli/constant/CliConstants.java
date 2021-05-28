package io.lematech.httprunner4j.cli.constant;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Constants
 * @description constants variables
 * @created 2021/5/2 10:14 下午
 * @publicWechat lematech
 */
public class CliConstants {
    /**
     * generate yml format testcase
     */
    public static final String GENERATE_YML_FORMAT = "2y";

    /**
     * generate json format testcase
     */
    public static final String GENERATE_JSON_FORMAT = "2j";

    /**
     * generate mode of full,All request header validation
     */
    public static final String GENERATE_MODE_FULL = "full";

    /**
     * generate mode of easy,Contains only content type validation
     */
    public static final String GENERATE_MODE_EASY = "easy";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String GENERATE_MODE_EASY_CONTENT_TYPE_META = "Content-Type";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String FILTER_REQUEST_SUFFIX_SEPARATOR = ";";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String PSEUDO_REQUEST_HEADER = ":";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String APPLICATION_JSON_MIME_TYPE = "application/json";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String APPLICATION_JSON_MIME_TYPE_UTF_8 = "application/json;charset=UTF-8";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String APPLICATION_ENCODING_BASE64 = "base64";

    /**
     * generate mode of easy,to content-type meta element
     */
    public static final String HAR_REQUEST_HEADER_COOKIE = "Cookie";


    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_APPLICATION_FILE_PATH_FOR_SPRINGBOOT = "vm/scaffold/springboot/application.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_APPLICATION_YML_FILE_PATH_FOR_SPRINGBOOT = "vm/scaffold/springboot/yml.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_SPRINGBOOT = "vm/scaffold/springboot/pom.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH_FOR_SPRINGBOOT = "vm/scaffold/springboot/ignore.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TEST_FILE_PATH_FOR_SPRINGBOOT = "vm/scaffold/springboot/test.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH_FOR_SPRINGBOOT = "vm/scaffold/springboot/package-info.vm";

    /**
     * Scaffolding formwork for hrun4j-api
     */
    public static final String SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_API = "vm/scaffold/hrun4j-api/meta-info/pom.vm";

    /**
     * Scaffolding formwork for hrun4j-api
     */
    public static final String SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH_FOR_API = "vm/scaffold/hrun4j-api/meta-info/ignore.vm";

    /**
     * Scaffolding formwork for hrun4j-api
     */
    public static final String SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_API = "vm/scaffold/hrun4j-api/meta-info/readme.vm";

    /**
     * Scaffolding formwork for hrun4j-api
     */
    public static final String SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FILE_PATH_FOR_API = "vm/scaffold/hrun4j-api/function/HttpRunner4j.vm";

    /**
     * Scaffolding formwork for hrun4j-api
     */
    public static final String SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FUNCTION_FILE_PATH_FOR_API = "vm/scaffold/hrun4j-api/function/functions/JokeFunction.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TEST_FILE_PATH_FOR_API = "vm/scaffold/springboot/test.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_PACKAGE_INFO_FILE_PATH_FOR_API = "vm/scaffold/springboot/package-info.vm";

}
