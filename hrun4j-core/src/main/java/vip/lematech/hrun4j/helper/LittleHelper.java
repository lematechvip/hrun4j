package vip.lematech.hrun4j.helper;



import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class LittleHelper<T> {

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

    /**
     * is windows ?
     * @return
     */
    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    /**
     * escapeRegexTransfer
     * @param dirPath
     * @return
     */
    public static String escapeRegexTransfer(String dirPath){
        if(!isWindows()){
            return dirPath;
        }
        char [] pathChars =dirPath.toCharArray();
        StringBuffer resultStr = new StringBuffer();
        for(char path : pathChars){
            if(path == '\\'){
                resultStr.append("\\\\");
            }else{
                resultStr.append(path);
            }
        }
        return resultStr.toString();
    }
}
