package io.lematech.httprunner4j.entity.testcase;

import lombok.Data;

import java.util.Objects;

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
    private Object check;
    private String comparator;
    private Object expect;

    /**
     * set comparator defualt is equalTo
     *
     * @param comparator
     */
    public void setComparator(String comparator) {
        if (Objects.isNull(this.comparator)) {
            this.comparator = "equalTo";
        }
        this.comparator = comparator;
    }
}
