package io.lematech.httprunner4j.control;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TeardownHook implements Hook{
    @Override
    public String hook() {
        log.info("teardown hook is executing");

        return null;
    }
}
