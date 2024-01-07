package top.flobby.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.flobby.live.common.utils.ConvertBeanUtils;
import top.flobby.live.gift.dto.RedPacketConfigDTO;
import top.flobby.live.gift.dto.RedPacketReqDTO;
import top.flobby.live.gift.interfaces.IRedPacketRpc;
import top.flobby.live.gift.provider.dao.po.RedPacketConfigPO;
import top.flobby.live.gift.provider.service.IRedPacketService;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2024-01-07 13:20
 **/

@DubboService
public class RedPacketRpcImpl implements IRedPacketRpc {

    @Resource
    private IRedPacketService redPacketService;

    @Override
    public RedPacketConfigDTO queryRedPacketConfigByAnchorId(Long anchorId) {
        RedPacketConfigPO redPacketConfig = redPacketService.queryRedPacketConfigByAnchorId(anchorId);
        return ConvertBeanUtils.convert(redPacketConfig, RedPacketConfigDTO.class);
    }

    @Override
    public boolean addRedPacketConfig(RedPacketReqDTO reqDTO) {
        RedPacketConfigPO redPacketConfig = ConvertBeanUtils.convert(reqDTO, RedPacketConfigPO.class);
        return redPacketService.addRedPacketConfig(redPacketConfig);
    }

    @Override
    public boolean updateRedPacketByAnchorId(RedPacketReqDTO reqDTO) {
        RedPacketConfigPO redPacketConfig = ConvertBeanUtils.convert(reqDTO, RedPacketConfigPO.class);
        return redPacketService.updateRedPacketByAnchorId(redPacketConfig);
    }
}
