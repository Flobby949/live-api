package top.flobby.live.im.core.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.flobby.live.im.core.server.common.ChannelHandlerContextCache;
import top.flobby.live.im.core.server.common.ImMsg;
import top.flobby.live.im.core.server.utils.ImContextUtils;

import static top.flobby.live.common.constants.ImCoreServerConstant.IM_BIND_IP_KEY;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 基本处理器
 * @create : 2023-12-07 15:24
 **/

@Component
@ChannelHandler.Sharable
public class ImServerCoreHandler extends SimpleChannelInboundHandler<Object> {

    @Resource
    private ImHandlerFactory imHandlerFactory;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // instanceof 增强写法，判断是否是ImMsg类型
        if (!(msg instanceof ImMsg imMsg)) {
            throw new IllegalArgumentException("msg is not instance of ImMsg, msg is " + msg.getClass().getName());
        }
        // 根据code，分发到不同的handler处理
        imHandlerFactory.doMsgHandler(ctx, imMsg);
    }

    /**
     * 正常或者异常断开连接，都会触发这个方法
     *
     * @param ctx CTX
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(appId)) {
            // 移除断线的ip连接记录
            redisTemplate.delete(IM_BIND_IP_KEY + appId + ":" + userId);
            ChannelHandlerContextCache.remove(userId);
        }
    }
}
