package vip.lematech.httprunner4j.widget.utils;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vip.lematech.httprunner4j.common.DefinedException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class SmallUtil<T> {


    /**
     * Replace the last qualified character according to the regular
     *
     * @param text        content text
     * @param regex       regular expression
     * @param replacement replacement text
     * @return Replace the following character
     */
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    public static Integer s2ms(Integer s) {
        if (Objects.isNull(s)) {
            return null;
        }
        Integer ms;
        try {
            ms = s * 1000;
        } catch (Exception e) {
            String exceptionMsg = String.format("ms to s occur exception：%s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return ms;
    }

    public static String getSimpleDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static Object emptyIfNull(Object obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        return obj;
    }

    public static String formatJsonOutput(Object output) {
        if (Objects.isNull(output)) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        } catch (JsonProcessingException e) {
            return String.valueOf(output);
        }
    }

    public static Object objectDeepCopy(Object obj, Class clz) {
        Object copyObj;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            copyObj = objectMapper.readValue(objectMapper.writeValueAsString(obj), clz);
        } catch (JsonProcessingException e) {
            String exceptionMsg = String.format("An exception occurred in the deep copy of the test case ,Exception Informations:  %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        } catch (IOException e) {
            String exceptionMsg = String.format("An exception occurred in the deep copy of the test case ,Exception Informations:  %s", e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return copyObj;
    }
}
