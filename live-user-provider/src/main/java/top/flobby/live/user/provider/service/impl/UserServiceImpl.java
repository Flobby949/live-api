package top.flobby.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.interfaces.utils.ConvertBeanUtils;
import top.flobby.live.user.interfaces.dto.UserDTO;
import top.flobby.live.user.provider.dao.mapper.UserMapper;
import top.flobby.live.user.provider.service.IUserService;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 实现类
 * @create : 2023-11-18 14:44
 **/

@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDTO getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
    }
}
