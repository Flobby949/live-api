package top.flobby.live.common.exception;

import lombok.Getter;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 自定义业务异常
 * @create : 2023-12-02 13:31
 **/

@Getter
public class BusinessException extends RuntimeException {

    private BusinessExceptionEnum e;

    public BusinessException(BusinessExceptionEnum e) {
        this.e = e;
    }

    public void setE(BusinessExceptionEnum e) {
        this.e = e;
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