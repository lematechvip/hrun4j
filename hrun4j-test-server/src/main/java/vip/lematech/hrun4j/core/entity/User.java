package vip.lematech.hrun4j.core.entity;

import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
@Data
public class User {
    private Integer uid;
    private String name;
    private String password;
}
