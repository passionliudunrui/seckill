package com.xxxx.seckill.vo;


import com.xxxx.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}
