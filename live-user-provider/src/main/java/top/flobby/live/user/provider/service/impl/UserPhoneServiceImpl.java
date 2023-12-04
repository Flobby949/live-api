package top.flobby.live.user.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import top.flobby.live.common.exception.BusinessException;
import top.flobby.live.common.exception.BusinessExceptionEnum;
import top.flobby.live.common.utils.CommonUtils;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.common.utils.DESUtils;
import top.flobby.live.common.utils.JwtUtil;
import top.flobby.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import top.flobby.live.id.enums.IdTypeEnum;
import top.flobby.live.id.interfaces.IdGenerateRpc;
import top.flobby.live.user.dto.UserDTO;
import top.flobby.live.user.dto.UserLoginDTO;
import top.flobby.live.user.dto.UserPhoneDTO;
import top.flobby.live.user.provider.dao.mapper.UserPhoneMapper;
import top.flobby.live.user.provider.dao.po.UserPhonePO;
import top.flobby.live.user.provider.service.IUserPhoneService;
import top.flobby.live.user.provider.service.IUserService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.flobby.live.user.constants.Constant.DEFAULT_AVATAR;
import static top.flobby.live.user.constants.Constant.USER_STATUS_EFFECTIVE;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-03 14:28
 **/

@Slf4j
@Service
public class UserPhoneServiceImpl implements IUserPhoneService {

    @Resource
    private UserPhoneMapper userPhoneMapper;
    @Resource
    private IUserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;
    @DubboReference
    private IdGenerateRpc idGenerateRpc;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginDTO login(String phone) {
        if (!CommonUtils.checkPhone(phone)) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        // 是否注册过
        UserPhoneDTO userPhoneDTO = queryByPhone(phone);
        if (!ObjectUtils.isEmpty(userPhoneDTO)) {
            log.info("已注册过，进行登录, phone: {}", phone);
            // 注册过直接返回
            return UserLoginDTO.loginSuccess(userPhoneDTO.getUserId(), createAndSaveUserToken(userPhoneDTO.getUserId()));
        }
        // 未注册过，添加记录
        log.info("未注册过，进行注册, phone: {}", phone);
        return registerAndLogin(phone);
    }

    @Override
    public void logout(String token) {
        String key = userProviderCacheKeyBuilder.buildUserLoginTokenKey(token);
        redisTemplate.delete(key);
    }

    /**
     * 注册并登录
     *
     * @param phone 电话
     * @return {@link UserLoginDTO}
     */
    private UserLoginDTO registerAndLogin(String phone) {
        Long unSeqId = idGenerateRpc.getUnSeqId(IdTypeEnum.USER_ID.getCode());
        log.info("生成用户ID: {}", unSeqId);
        if (ObjectUtils.isEmpty(unSeqId)) {
            throw new BusinessException(BusinessExceptionEnum.ID_GENERATE_ERROR);
        }
        userService.insertUser(UserDTO.builder()
                .userId(unSeqId)
                .nickName("用户" + unSeqId)
                .avatar(DEFAULT_AVATAR)
                .build());
        userPhoneMapper.insert(UserPhonePO.builder().userId(unSeqId).phone(DESUtils.encrypt(phone)).status(USER_STATUS_EFFECTIVE).build());
        // 删除空值缓存
        redisTemplate.delete(userProviderCacheKeyBuilder.buildUserPhoneObjKey(phone));
        return UserLoginDTO.loginSuccess(unSeqId, createAndSaveUserToken(unSeqId));
    }

    /**
     * 创建和保存用户令牌
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    private String createAndSaveUserToken(Long userId) {
        String token = JwtUtil.createToken(userId);
        // 构造key  xxx:xxx:token -> userId
        String key = userProviderCacheKeyBuilder.buildUserLoginTokenKey(token);
        redisTemplate.opsForValue().set(key, userId, 48, TimeUnit.HOURS);
        return token;
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        if (!CommonUtils.checkPhone(phone)) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        // 先从缓存中查询
        String key = userProviderCacheKeyBuilder.buildUserPhoneObjKey(phone);
        UserPhoneDTO userPhoneDTO = (UserPhoneDTO) redisTemplate.opsForValue().get(key);
        if (!ObjectUtils.isEmpty(userPhoneDTO)) {
            // 如果是空值缓存
            if (ObjectUtils.isEmpty(userPhoneDTO.getUserId())) {
                return null;
            }
            log.info("缓存命中");
            return userPhoneDTO;
        }
        // 缓存中没有，从数据库中查询
        userPhoneDTO = queryByPhoneFromDB(phone);
        if (!ObjectUtils.isEmpty(userPhoneDTO)) {
            // 解密
            userPhoneDTO.setPhone(DESUtils.decrypt(userPhoneDTO.getPhone()));
            // 数据库中有，放入缓存
            redisTemplate.opsForValue().set(key, userPhoneDTO, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
            return userPhoneDTO;
        }
        // 防止缓存穿透，空值缓存
        userPhoneDTO = new UserPhoneDTO();
        redisTemplate.opsForValue().set(key, userPhoneDTO, 60 * 5, TimeUnit.SECONDS);
        return null;
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        if (ObjectUtils.isEmpty(userId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAMS_ERROR);
        }
        String key = userProviderCacheKeyBuilder.buildUserPhoneListKey(userId);
        List<Object> userPhoneDTOList = redisTemplate.opsForList().range(key, 0, -1);
        if (!ObjectUtils.isEmpty(userPhoneDTOList)) {
            // 如果是空值缓存
            if (ObjectUtils.isEmpty(((UserPhoneDTO) userPhoneDTOList.get(0)).getId())) {
                return Collections.emptyList();
            }
            log.info("缓存命中");
            return userPhoneDTOList.stream().map(UserPhoneDTO.class::cast).collect(Collectors.toList());
        }
        // 缓存中没有，从数据库中查询
        List<UserPhoneDTO> userPhoneDTOListFromDB = queryByUserIdFromDB(userId);
        if (!CollectionUtils.isEmpty(userPhoneDTOListFromDB)) {
            // 解密
            userPhoneDTOListFromDB.forEach(item -> item.setPhone(DESUtils.decrypt(item.getPhone())));
            redisTemplate.opsForList().leftPushAll(key, userPhoneDTOListFromDB.toArray());
            redisTemplate.expire(key, CommonUtils.createRandomExpireTime(), TimeUnit.SECONDS);
            return userPhoneDTOListFromDB;
        }
        // 防止缓存穿透，空值缓存
        redisTemplate.opsForList().leftPushAll(key, new UserPhoneDTO());
        redisTemplate.expire(key, 60 * 5, TimeUnit.SECONDS);
        return Collections.emptyList();
    }

    /**
     * 从数据库按用户ID查询
     *
     * @param userId 用户 ID
     * @return {@link List}<{@link UserPhoneDTO}>
     */
    public List<UserPhoneDTO> queryByUserIdFromDB(Long userId) {
        LambdaQueryWrapper<UserPhonePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPhonePO::getUserId, userId).eq(UserPhonePO::getStatus, USER_STATUS_EFFECTIVE);
        List<UserPhonePO> userPhonePOList = userPhoneMapper.selectList(wrapper);
        log.info("数据库查询");
        return ConvertBeanUtils.convertList(userPhonePOList, UserPhoneDTO.class);
    }

    /**
     * 从数据库通过手机号查询
     *
     * @param phone 电话
     * @return {@link UserPhoneDTO}
     */
    public UserPhoneDTO queryByPhoneFromDB(String phone) {
        LambdaQueryWrapper<UserPhonePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPhonePO::getPhone, DESUtils.encrypt(phone)).eq(UserPhonePO::getStatus, USER_STATUS_EFFECTIVE);
        UserPhonePO userPhone = userPhoneMapper.selectOne(wrapper);
        log.info("数据库查询， userPhone: {}", userPhone);
        return ConvertBeanUtils.convert(userPhone, UserPhoneDTO.class);
    }
}
