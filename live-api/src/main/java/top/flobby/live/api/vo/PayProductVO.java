package top.flobby.live.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : Flobby
 * @program : live-api
 * @description :
 * @create : 2023-12-20 08:47
 **/

@Data
public class PayProductVO {
    private Integer currentBalance;
    private List<PayProductItemVO> productList;
}
