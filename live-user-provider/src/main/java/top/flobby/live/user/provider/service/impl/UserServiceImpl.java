package top.flobby.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.flobby.live.common.interfaces.utils.ConvertBeanUtils;
import top.flobby.live.user.interfaces.dto.UserDTO;
import top.flobby.live.user.provider.dao.mapper.UserMapper;
import top.flobby.live.user.provider.dao.po.UserPO;
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

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        return userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class)) > 0;
    }

    @Override
    public boolean insertUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        return userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class)) > 0;
    }
}
