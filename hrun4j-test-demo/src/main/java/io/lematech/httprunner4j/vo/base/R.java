package io.lematech.httprunner4j.vo.base;

import io.lematech.httprunner4j.core.enums.BusinessCode;
import io.lematech.httprunner4j.core.enums.CommonBusinessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lematech
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R<T> implements Serializable {
    /**
     * 错误码
     */
    private String code;
    /**
     * 响应提示信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    /**
     * 业务处理异常,自定义业务响应码及响应提示信息
     *
     * @param businessCode
     * @return
     */
    public static R fail(BusinessCode businessCode) {
        return new R(businessCode);
    }

    /**
     * 业务处理异常，系统错误
     *
     * @return
     */
    public static R fail() {
        return new R(CommonBusinessCode.SYS_ERROR_EXCEPTION);
    }

    /**
     * 业务处理异常，按指定异常信息提示
     *
     * @param message
     * @return
     */
    public static R fail(String message) {
        R restResponseResult = fail();
        restResponseResult.setMessage(message);
        return restResponseResult;
    }

    /**
     * 响应成功，返回响应数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(T data) {
        R restResponseResult = ok();
        restResponseResult.setData(data);
        return restResponseResult;
    }

    /**
     * 自定义响应提示信息
     *
     * @return
     */
    public static R ok(String message) {
        R restResponseResult = ok();
        restResponseResult.setMessage(message);
        return restResponseResult;
    }

    /**
     * 响应成功，不返回响应数据
     *
     * @return
     */
    public static R ok() {
        return new R(CommonBusinessCode.SUCCESS);
    }

    public R(BusinessCode businessCode) {
        this.code = businessCode.getCode();
        this.message = businessCode.getMessage();
    }
}
