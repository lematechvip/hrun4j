package io.lematech.httprunner4j.core;

import io.lematech.httprunner4j.core.entity.User;
import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className UserDto
 * @description TODO
 * @created 2021/3/3 2:39 下午
 * @publicWechat lematech
 */
@Data
public class UserDto {
    private int index;
    private User user;
}
