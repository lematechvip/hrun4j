package io.lematech.httprunner4j.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className PrintMap
 * @description TODO
 * @created 2021/1/22 4:07 下午
 * @publicWechat lematech
 */
@Slf4j
public class PrintMap {
    public static void printMap(Map<String, String> map) {
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                log.debug(entry.getKey() + "=" + entry.getValue());
            }
        }
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
}
