package vip.lematech.hrun4j.cli.helper;

import com.google.gson.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class GsonHepler {
    private static final Map<FieldNamingPolicy, Gson> gsons = new HashMap();
    private static final JsonSerializer<Date> serializer = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());
    private static final JsonDeserializer<Date> deserializer = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());

    public GsonHepler() {
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
