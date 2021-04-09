package io.lematech.httprunner4j.core.entity;

import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className User
 * @description TODO
 * @created 2021/3/3 2:09 下午
 * @publicWechat lematech
 */
@Data
public class User {
    private Integer uid;
    private String name;
    private String password;
}
