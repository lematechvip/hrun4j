package io.lematech.httprunner4j.cli.model.har;

import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className HarBaseModel
 * @description Har base model
 * @created 2021/5/3 9:41 下午
 * @publicWechat lematech
 */
@Data
public class HarBaseModel {
    String name;

    String value;

    String comment;
}
