package vip.lematech.hrun4j.core.enums;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
public enum CommonBusinessCode implements BusinessCode {
    /**
     * 公共领域
     */
    SUCCESS("00", "ok！"),
    Authorization_FAILED_EXCEPTION("403", "认证失败！"),
    USER_IS_NOT_EXISTS_EXCEPTION("404", "用户不存在！"),
    SYS_ERROR_EXCEPTION("09", "系统错误！");
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应提示信息
     */
    private String message;

    CommonBusinessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {

        return this.message;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
