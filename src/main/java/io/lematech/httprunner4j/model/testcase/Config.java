package io.lematech.httprunner4j.model.testcase;

import io.lematech.httprunner4j.model.base.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Config
 * @description TODO
 * @created 2021/1/20 2:25 下午
 * @publicWechat lematech
 */
@NoArgsConstructor
@Data
public class Config extends BaseModel {
    /**
     * 必填
     */
    private String name;

    private String baseUrl;

    private Boolean verify;

}
