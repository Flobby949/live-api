package top.flobby.live.common.resp;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 统一返回结果
 * @create : 2023-12-02 13:31
 **/

@Data
public class CommonResp<T> {
    /**
     * 业务成功或失败
     */
    private Boolean success = true;
    /**
     * 返回信息
     */
    private String message = "请求成功";
    /**
     * 返回数据
     */
    private T data;

    public static <T> CommonResp<T> success(T data) {
        CommonResp<T> res = new CommonResp<>();
        res.setData(data);
        return res;
    }

    public static <T> CommonResp<T> success() {
        return success(null);
    }

    public static <T> CommonResp<T> error(String msg, T data) {
        CommonResp<T> res = new CommonResp<>();
        res.setSuccess(false);
        res.setMessage(msg);
        res.setData(data);
        return res;
    }

    public static <T> CommonResp<T> error(String msg) {
        return error(msg, null);
    }

    public static <T> CommonResp<T> error() {
        return error("请求失败！", null);
    }

}
