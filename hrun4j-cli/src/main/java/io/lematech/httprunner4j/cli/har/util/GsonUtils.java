package io.lematech.httprunner4j.cli.har.util;

import com.google.gson.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className GsonUtils
 * @description Gson tools
 * @created 2021/5/2 10:00 下午
 * @publicWechat lematech
 */
public class GsonUtils {
    private static final Map<FieldNamingPolicy, Gson> gsons = new HashMap();
    private static final JsonSerializer<Date> serializer = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());
    private static final JsonDeserializer<Date> deserializer = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());

    public GsonUtils() {
    }

    public static Gson getGson() {
        return getGson(FieldNamingPolicy.IDENTITY);
    }

    public static Gson getGson(FieldNamingPolicy fieldNamingPolicy) {
        if (gsons.containsKey(fieldNamingPolicy)) {
            return gsons.get(fieldNamingPolicy);
        } else {
            Gson gson = (new GsonBuilder()).setFieldNamingPolicy(fieldNamingPolicy).registerTypeAdapter(Date.class, serializer).registerTypeAdapter(Date.class, deserializer).create();
            synchronized (gsons) {
                gsons.put(fieldNamingPolicy, gson);
                return gson;
            }
        }
    }
}
