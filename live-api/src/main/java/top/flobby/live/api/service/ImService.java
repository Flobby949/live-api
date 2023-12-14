package top.flobby.live.api.service;

import top.flobby.live.api.vo.ImConfigVO;

/**
 * @author : Flobby
 * @program : live-api
 * @description : IM 服务
 * @create : 2023-12-13 17:59
 **/

public interface ImService {

    /**
     * 获取 IM 配置
     *
     * @return {@link ImConfigVO}
     */
    ImConfigVO getImConfig();
}
