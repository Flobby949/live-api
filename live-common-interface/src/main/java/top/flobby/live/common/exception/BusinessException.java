package top.flobby.live.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 自定义业务异常
 * @create : 2023-12-02 13:31
 **/

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private String errMsg;

    public BusinessException(BusinessExceptionEnum e) {
        this.errMsg = e.getDesc();
    }

    public BusinessException(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * 不写入堆栈信息，提高性能
     *
     * @return {@link Throwable}
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}