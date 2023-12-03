package top.flobby.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 用户登录返回结果
 * @create : 2023-12-03 14:21
 **/

@Data
public class UserLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5748526878339007228L;

    private Long userId;
    private String token;
    private Boolean isLoginSuccess;
    private String message;

    /**
     * 登录失败
     *
     * @param message 消息
     * @return {@link UserLoginDTO}
     */
    public static UserLoginDTO loginError(String message) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setIsLoginSuccess(false);
        userLoginDTO.setMessage(message);
        return userLoginDTO;
    }

    /**
     * 登录成功
     *
     * @param userId 用户 ID
     * @param token  令 牌
     * @return {@link UserLoginDTO}
     */
    public static UserLoginDTO loginSuccess(Long userId, String token) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setIsLoginSuccess(true);
        userLoginDTO.setUserId(userId);
        userLoginDTO.setToken(token);
        return userLoginDTO;
    }
}
