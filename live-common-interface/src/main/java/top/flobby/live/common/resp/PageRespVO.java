package top.flobby.live.common.resp;

import lombok.Data;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 分页返回VO
 * @create : 2023-12-12 09:27
 **/

@Data
public class PageRespVO<T> {
    private List<T> list;
    private boolean hasNext;
}
