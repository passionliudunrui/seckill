package com.xxxx.seckill.vo;

import com.xxxx.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

    private Order order;
    private GoodsVo goodsVo;
}

