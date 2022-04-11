package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    //秒杀
    @Transactional
    @Override
    public Order secKill(User user, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //1.减库存
        SeckillGoods seckillGoods=
                seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);

        //对于库存的判断  解决超卖问题  这个语法不太懂
        //seckillGoodsService.updateById(seckillGoods);

        boolean result=seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count ="+"stock_count-1").eq("goods_id",goods.getId()).gt("stock_count",0));

        if(seckillGoods.getStockCount()<1){
            valueOperations.set("isStockEmpty:"+goods.getId(),"0");

            return null;
        }

        //2.生成订单
        Order order=new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //3.生成秒杀订单
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);

        //将订单存到了redis中
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);

        return order;

    }


    /**
     * 订单详情
     * @param orderId
     * @return
     */

    @Override
    public OrderDetailVo detail(Long orderId) {
        //获取商品订单
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.SESSION_ERREO);
        }

        Order order=orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());

        OrderDetailVo detail=new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;

    }
}
