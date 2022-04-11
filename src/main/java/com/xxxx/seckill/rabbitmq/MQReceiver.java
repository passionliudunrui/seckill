package com.xxxx.seckill.rabbitmq;


import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.utils.JsonUtils;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import com.xxxx.seckill.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;

    /**
     * 下单操作 秒杀消息
     */

    @RabbitListener(queues = "seckillQueue")
    public void reveive(String message) throws Exception {
        log.info("接收消息"+message);
        SeckillMessage seckillMessage = JsonUtils.objectFromJsonStr(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        //判断是否还有库存
        GoodsVo goodsVo= goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return;
        }
        //下单操作
        orderService.secKill(user,goodsVo);

    }

}
