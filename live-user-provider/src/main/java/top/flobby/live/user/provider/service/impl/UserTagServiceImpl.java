package top.flobby.live.user.provider.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.flobby.live.user.constants.UserTagsEnum;
import top.flobby.live.user.provider.dao.mapper.UserTagMapper;
import top.flobby.live.user.provider.dao.po.UserTagPO;
import top.flobby.live.user.provider.service.IUserTagService;
import top.flobby.live.user.utils.TagInfoUtils;

import static top.flobby.live.user.constants.Constant.*;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-11-28 12:02
 **/

@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private UserTagMapper userTagMapper;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagMapper.removeTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
    }

    @Override
    public boolean containsTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagPO tag = userTagMapper.selectById(userId);
        if (ObjectUtils.isEmpty(tag)) {
            return false;
        }
        String fieldName = userTagsEnum.getFieldName();

        Long tagInfo = switch (fieldName) {
            case TAG_INFO_01 -> tag.getTagInfo01();
            case TAG_INFO_02 -> tag.getTagInfo02();
            case TAG_INFO_03 ->  tag.getTagInfo03();
            default -> throw new IllegalStateException("Unexpected value: " + fieldName);
        };
        return TagInfoUtils.isContain(tagInfo, userTagsEnum.getTag());
    }
}
