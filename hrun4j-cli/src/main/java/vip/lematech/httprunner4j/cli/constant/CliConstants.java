package vip.lematech.httprunner4j.cli.constant;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
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
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/meta-info/pom.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_IGNORE_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/meta-info/ignore.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_CLI = "vm/scaffold/httprunner4j/meta-info/readme_cli.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_README_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/meta-info/readme_pom.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/function/HttpRunner4j.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_HTTPRUNNER4J_FUNCTION_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/function/functions/JokeFunction.vm";


    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_JOKE_TEST_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testcase/joke/JokeTest.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RAP2_TEST_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testcase/rap2/MockTest.vm";


    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTDATA_APIS_GET_JOKE_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testdata/apis/getJoke.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTDATA_APIS_GET_SINGLE_JOKE_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testdata/apis/getSingleJoke.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTDATA_TESTCASE_APIS_JOKE_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testdata/testcases/joke/lookTheJokeFromJokeList.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTDATA_TESTCASE_APIS_RAP2_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testdata/testcases/rap2/rap2Mock.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTSUITE_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testsuite/testsuite.vm";


    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTSUITE_FILE_PATH_FOR_CLI = "vm/scaffold/httprunner4j/testsuite/testsuite_cli.vm";


    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTSUITE_JOKE_FILE_PATH_FOR_API = "vm/scaffold/httprunner4j/testsuite/testsuite_joke.vm";

    /**
     * api mode
     */
    public static final String HTTPRUNNER4J_API_TYPE = "POM";

    /**
     * command line mode
     */
    public static final String HTTPRUNNER4J_CLI_TYPE = "CLI";

    /**
     * command line mode
     */
    public static final String SRPINGBOOT_PROJECT_TYPE = "SRPINGBOOT";

}
