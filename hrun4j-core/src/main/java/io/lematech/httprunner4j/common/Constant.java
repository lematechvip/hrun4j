package io.lematech.httprunner4j.common;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Constant
 * @description TODO
 * @created 2021/1/20 10:50 上午
 * @publicWechat lematech
 */
public class Constant {
    /**
     * self-defined environment variable
     */
    public static final String ENV_FILE_NAME = ".env";
    /**
     * root package name
     */
    public static final String SELF_ROOT_PKG_NAME = "io.lematech.httprunner4j";
    public static final String TEST_TEMPLATE_FILE_PATH = "testClass.vm";
    public static final String TEST_CASE_FILE_PATH = "/testcases";
    public static final String SUPPORT_TEST_CASE_FILE_EXT_JSON_NAME = "json";
    public static final String SUPPORT_TEST_CASE_FILE_EXT_YML_NAME = "yml";
    public static final String TEST_CASE_DIRECTORY_NAME = "testcases";
    public static final String TEST_CASE_SCHEMA = "/schemas/testcase.schema.v2.json";
    public static final String API_MODEL_SCHEMA = "/schemas/api.schema.json";
    public static final String REGEX_EXPRESSION = "(?<=\\$\\{).*?(?=})";
    public static final String REGEX_EXPRESSION_FLAG = "(.*)\\$\\{(.*?)\\}(.*)";
    public static final String REGEX_ENV_EXPRESSION_FLAG = "(.*)\\$\\{ENV(.*?)\\}(.*)";
    public static final String REGEX_EXPRESSION_REPLACE = "\\$\\{.*?}";
    public static final String DOT_PATH = ".";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String I18N_CN = "US";
    public static final String I18N_US = "CN";
    public static final String URL_REGEX = "http(s)?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";


}
