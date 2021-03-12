package io.lematech.httprunner4j.entity.testcase;

import lombok.Data;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Comparator
 * @description TODO
 * @created 2021/1/20 5:01 下午
 * @publicWechat lematech
 */
@Data
public class Comparator {
    private String check;
    private String comparator;
    private Object expect;
}
