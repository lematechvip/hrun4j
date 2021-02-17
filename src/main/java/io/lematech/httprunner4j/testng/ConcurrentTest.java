package io.lematech.httprunner4j.testng;

import org.testng.annotations.Test;

import java.util.UUID;

public class ConcurrentTest {
    @Test(threadPoolSize = 10, invocationCount = 10, timeOut = 1000)
    public static void display() {
        System.out.println(UUID.randomUUID().toString());
    }
}
