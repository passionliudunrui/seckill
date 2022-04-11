package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-04-04
 */

@SuppressWarnings("all")
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        //通过userid和goodsid获取orderid

        //获取秒杀订单
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(
                new QueryWrapper<SeckillOrder>().eq("user_id",
                        user.getId()).eq("goods_id", goodsId)
        );
        System.out.println("44444444444444");
        if(null!=seckillOrder){
            System.out.println("5555555555555");
            return seckillOrder.getOrderId();
        }
        else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            System.out.println("7777777777777777");
            return -1L;
        }
        else{
            System.out.println("8888888888888");
            return 0L;
        }

    }
}
