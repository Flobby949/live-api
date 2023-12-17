package top.flobby.live.gift.provider.service;

import lombok.Data;

import java.io.Serial;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 缓存延迟双删BO
 * @create : 2023-12-17 16:24
 **/

@Data
public class GiftCacheRemoveBO implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -8180043041726487166L;

    private boolean removeListCache;
    private int giftId;
}
