package vip.lematech.httprunner4j.entity.http;

/**
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public @interface HttpConstant {
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String HEAD = "HEAD";
    String PATCH = "PATCH";
    String value();
    int CONNECT_TIME_OUT = 10000;
    int SOCKET_TIME_OUT = 10000;
    int CONNECTION_REQUEST_TIME_OUT = 10000;
}