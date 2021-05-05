package io.lematech.httprunner4j.cli;

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
    public static final String APPLICATION_JSON_MIME_TYPE = "application/json";

}
