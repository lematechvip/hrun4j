package vip.lematech.hrun4j.cli.constant;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
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

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

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
    public static final String SCAFFOLD_TEMPLATE_POM_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/pom.vm";


    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_HRUN4J_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/function/Hrun4j.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_HRUN4J_FUNCTION_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/function/functions/MyFunction.vm";


    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_JOKE_TEST_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/testcase/get/GetTest.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RAP2_TEST_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/testcase/post/PostTest.vm";

    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTSUITE_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/testsuite/testsuite.vm";


    /**
     * Scaffolding formwork for springboot
     */
    public static final String SCAFFOLD_TEMPLATE_TESTSUITE_ALL_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/template/testsuite/testsuite_all.vm";


    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_APIS_GET_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/apis/get.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_FORM_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/apis/postFormData.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_RAW_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/apis/postRawText.vm";


    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_DATA_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/data/csvFile.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_GET_GETSCENE_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/testcases/get/getScene.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_POST_POSTSCENE_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/testcases/post/postScene.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_HR_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/resources/Hrun4j.vm";


    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_IGNORE_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/meta-info/gitignore.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_README_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/meta-info/readMe.vm";

    /**
     * Scaffolding formwork for hrun4j-pom
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_ENV_FILE_PATH_FOR_API = "vm/scaffold/hrun4j/pom/meta-info/env.vm";


    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_APIS_GET_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/apis/get.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_FORM_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/apis/postFormData.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_APIS_POST_RAW_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/apis/postRawText.vm";


    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_DATA_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/data/csvFile.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_BSH_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/bsh/test.vm";


    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_GET_GETSCENE_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/testcases/get/getScene.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_TESTCASES_POST_POSTSCENE_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/testcases/post/postScene.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_HR_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/Hrun4j.vm";


    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_IGNORE_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/gitignore.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_README_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/readMe.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_TESTSUITE_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/testsuite/testsuite.vm";

    /**
     * Scaffolding formwork for hrun4j-cli
     */
    public static final String SCAFFOLD_TEMPLATE_RESOURCES_ENV_FILE_PATH_FOR_CLI = "vm/scaffold/hrun4j/cli/env.vm";


    /**
     * api mode
     */
    public static final String HRUN4J_POM_TYPE = "POM";

    /**
     * command line mode
     */
    public static final String HRUN4J_CLI_TYPE = "CLI";

    /**
     * command line mode
     */
    public static final String SRPINGBOOT_PROJECT_TYPE = "SRPINGBOOT";

}
