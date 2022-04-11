package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author passion
 * @since 2022-04-04
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    //获取秒杀结果   通过数据库返回
    Long getResult(User user, Long goodsId);
}
