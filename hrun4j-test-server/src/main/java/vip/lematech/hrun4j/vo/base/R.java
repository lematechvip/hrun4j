package vip.lematech.hrun4j.vo.base;

import lombok.*;
import vip.lematech.hrun4j.core.enums.BusinessCode;
import vip.lematech.hrun4j.core.enums.CommonBusinessCode;

import java.io.Serializable;

/**
 * @author lematech
 */
@EqualsAndHashCode
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
     * @param businessCode 业务码
     * @return 响应数
     */
    public static R fail(BusinessCode businessCode) {
        return new R(businessCode);
    }

    /**
     * 业务处理异常，系统错误
     *
     * @return 响应数据
     */
    public static R fail() {
        return new R(CommonBusinessCode.SYS_ERROR_EXCEPTION);
    }

    /**
     * 业务处理异常，按指定异常信息提示
     *
     * @param message 提升信息
     * @return 响应数据
     */
    public static R fail(String message) {
        R restResponseResult = fail();
        restResponseResult.setMessage(message);
        return restResponseResult;
    }

    /**
     * 响应成功，返回响应数据
     *
     * @param data 响应数据
     * @param <T> 所属类型
     * @return 响应结果
     */
    public static <T> R<T> ok(T data) {
        R restResponseResult = ok();
        restResponseResult.setData(data);
        return restResponseResult;
    }

    /**
     * 自定义响应提示信息
     * @param message message
     * @return 响应数据
     */
    public static R ok(String message) {
        R restResponseResult = ok();
        restResponseResult.setMessage(message);
        return restResponseResult;
    }

    /**
     * 响应成功，不返回响应数据
     *
     * @return 响应数据
     */
    public static R ok() {
        return new R(CommonBusinessCode.SUCCESS);
    }

    public R(BusinessCode businessCode) {
        this.code = businessCode.getCode();
        this.message = businessCode.getMessage();
    }
}
