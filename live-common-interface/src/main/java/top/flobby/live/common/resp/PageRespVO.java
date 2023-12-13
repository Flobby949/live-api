package top.flobby.live.common.resp;

import lombok.Data;

import java.io.Serial;
import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description : 分页返回VO
 * @create : 2023-12-12 09:27
 **/

@Data
public class PageRespVO<T> implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -7149241748729239741L;
    
    private List<T> list;
    private boolean hasNext;
}
