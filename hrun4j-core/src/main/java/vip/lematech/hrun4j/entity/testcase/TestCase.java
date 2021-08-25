package vip.lematech.hrun4j.entity.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

@Data
public class TestCase {
    private Config config;
    @JSONField(name = "teststeps")
    @JsonProperty(value = "teststeps")
    private List<TestStep> testSteps;
}
