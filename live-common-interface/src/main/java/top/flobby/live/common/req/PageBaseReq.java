package top.flobby.live.common.req;

import lombok.Data;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 分页REQ
 * @create : 2023-12-12 09:30
 **/

@Data
public class PageBaseReq {

    private int page;
    private int pageSize;
}
