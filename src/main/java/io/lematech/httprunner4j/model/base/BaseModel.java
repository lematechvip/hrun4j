package io.lematech.httprunner4j.model.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className BaseModel
 * @description TODO
 * @created 2021/1/20 4:41 下午
 * @publicWechat lematech
 */
@Data
public class BaseModel {
    /**
     * TODO: 2021/1/20 可选键值对、列表、字符串，暂定义为键值对
     */
    @JsonProperty(value = "setup_hooks")
    private HashMap<String,Object> setupHooks;
    /**
     * TODO: 2021/1/20 可选键值对、列表、字符串，暂定义为键值对
     */
    @JsonProperty(value = "teardown_hooks")
    private HashMap<String,Object> teardownHooks;

    /**
     * TODO: 2021/1/20 可选键值对、列表、字符串，暂定义为键值对
     */
    @JsonProperty(value = "variables")
    private Map<String, Object> variables;
}
