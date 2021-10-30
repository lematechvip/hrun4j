package vip.lematech.hrun4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Data
public class TestSuiteCase<T> {
    private String name;
    @JsonProperty(value = "variables")
    private T variables;
    private T parameters;
    @JsonProperty(value = "testcase")
    @JSONField(name = "testcase")
    private String caseRelativePath;
}
