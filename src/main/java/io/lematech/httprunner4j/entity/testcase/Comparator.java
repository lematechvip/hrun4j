package io.lematech.httprunner4j.entity.testcase;

import lombok.Data;

@Data
public class Comparator {
    private String check;
    private String comparator;
    private Object expect;
}
