package io.lematech.httprunner4j.entity.http;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public @interface HttpConstant {
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String HEAD = "HEAD";
    String OPTIONS = "OPTIONS";
    String value();
    int CONNECT_TIME_OUT = 10000;
    int SOCKET_TIME_OUT = 10000;
    int CONNECTION_REQUEST_TIME_OUT = 10000;
}