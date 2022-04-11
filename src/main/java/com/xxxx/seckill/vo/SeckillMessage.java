package com.xxxx.seckill.vo;

import com.xxxx.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * 发送给RabbitMQ的消息
 * goodsId是指的
 */
public class SeckillMessage {

    private User user;
    private Long goodsId;
}
