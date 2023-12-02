package top.flobby.live.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.flobby.live.common.resp.CommonResp;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 统一异常处理
 * @create : 2023-12-02 13:35
 **/

@ControllerAdvice
public class ControllerExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 所有异常的处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(Exception e) {
        logger.error("系统异常：{}", e.getMessage());
        CommonResp<?> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getMessage());
        return commonResp;
    }


    /**
     * 业务异常的处理
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(BusinessException e) {
        logger.error("业务异常：{}", e.getE().getDesc());
        CommonResp<?> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getE().getDesc());
        return commonResp;
    }

}

