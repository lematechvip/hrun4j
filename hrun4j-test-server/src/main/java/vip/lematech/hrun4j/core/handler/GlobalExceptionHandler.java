package vip.lematech.hrun4j.core.handler;


import vip.lematech.hrun4j.core.exception.PlatformException;
import vip.lematech.hrun4j.vo.base.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = PlatformException.class)
    public R bizExceptionHandler(PlatformException platformException) {
        log.error("响应异常码：{}，响应异常信息：{}", platformException.getMessage(), platformException.getLocalizedMessage());
        return R.fail(platformException.getMsg() == null ? platformException.getMessage() : platformException.getMsg());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public R methodArgumentExceptionHandler(MethodArgumentTypeMismatchException exception) {
        String exceptionMsg = String.format("参数%s类型不匹配异常：%s", exception.getName(), exception.getMessage());
        log.error(exceptionMsg);
        return R.fail(exceptionMsg);
    }

    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception exception) {
        exception.printStackTrace();
        log.error("未知异常：{}", exception.getMessage());
        return R.fail(exception.getMessage());
    }

}
