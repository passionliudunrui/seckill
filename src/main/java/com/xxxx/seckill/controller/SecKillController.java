package com.xxxx.seckill.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.JsonUtils;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import com.xxxx.seckill.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;


    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisScript<Long> script;

    //在内存中做判断  进行内存的标记  (商品id)+(商品是否有货的boolean值)
    private Map<Long,Boolean> EmptyStockMap=new HashMap<>();

    /**
     * 功能描述 执行秒杀功能
     * @param model
     * @param user
     * @param goodsId
     * @return
     */

//    @RequestMapping(value = "/doSeckill")
//    public String doSecKill2(Model model, User user, Long goodsId){
//        if(user==null){
//            return "login";
//        }
//        model.addAttribute("user",user);
//
//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存是否足够
//        if(goods.getStockCount()<1){
//            //秒杀失败
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return "secKillFail";
//        }
//        //判断是否重复抢购
//        SeckillOrder secKillOrder=
//                seckillOrderService.getOne(
//                        new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId)
//                );
//        if(secKillOrder!=null){
//            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        Order order=orderService.secKill(user,goods);
//
//        model.addAttribute("order",order);
//        model.addAttribute("goods",goods);
//        return "orderDetail";
//
//    }

    /**
     * 功能描述：秒杀功能的优化
     * @param
     * @param user
     * @param goodsId
     * @return
     */


            /*
        库存数量的判断优化
        1.在商品初始化的时候就加载到Redis当中。
        2.不直接访问Mysql。
        比如说某商品的数量是10，那么Redis扣完10个商品后直接给用户返回数量不足
        不需要去判断数据库的库存

        如果库存是比较充足的，那么引入了RabbitMQ来处理
        将这个请求封装成一个对象，发送给RabbitMQ
        因为最终还要生成订单
        异步的好处：
        1.前期有大量的请求过来，我可以处理。 返回排队中。。。
        2.然后RabbitMQ慢慢的处理。 流量削峰
        3.然后生成订单减少库存
        4.客户端轮询，然后判断是否是真的秒杀成功。
         */

//    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
//    @ResponseBody
//    public RespBean doSecKill01( User user, Long goodsId){
//        //System.out.println("接收请求");
//        if(user==null){
//            return RespBean.error(RespBeanEnum.SESSION_ERREO);
//        }
//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存是否足够
//        if(goods.getStockCount()<1){
//            //秒杀失败
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//
//        /*判断是否重复抢购  如果是一个用户点击很快的话，可能会一个人买两件 方法1： 在数据库中添加组合索引（gid，uid）
//            方法二：通过redis进行缓存已经抢购订单的信息  然后在redis中判断是否抢购成功
//        */
////        SeckillOrder secKillOrder=
////                seckillOrderService.getOne(
////                        new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId)
////                );
//        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
//        if(seckillOrder!=null){
//            //model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
//            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
//        }
//        Order order=orderService.secKill(user,goods);
//        return RespBean.success(order);
//
//    }



    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(User user, Long goodsId, @PathVariable String path) throws Exception {
        //System.out.println("接收请求");
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERREO);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check=orderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUESE_ILLEGAL);
        }


        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //预先减去库存  decrement获取的是递减之后的库存数量
        //Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //库存减完了  =0的时候就是还有1件卖出去
        //=-1 就是没抢到


        Long stock=(Long)redisTemplate.execute(script, Collections.singletonList("seckillGoods:"+goodsId),Collections.EMPTY_LIST);


        //通过内存标记 减少Redis的访问
        if(EmptyStockMap.get(goodsId)){

            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        if(stock<0){
            //让库存=0

            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //正常来说是下单   但是引入了RabbitMQ
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtils.objectToJsonStr(seckillMessage));

        //客户端得到这个状态的时候 应该前端展示 正在排队中
        return RespBean.success(0);

    }

    /**
     * 功能描述：获取秒杀结果
     * @param user
     * @param goodsId  有的话成功（获取了结果）  -1秒杀失败  0 排队中
     * @return
     */
    @RequestMapping(value = "getResult",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){


        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERREO);
        }

        Long orderId=seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }


    /**
     *Redis初始化能够执行的方法  把商品库存数量加载到Redis中
     * 第一步还是要从数据库中取出来具体的Goods的相关信息
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list=goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for(GoodsVo goodsVo:list){
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
        }

    }


    /**
     * 功能描述：获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */

    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user,Long goodsId,String captcha){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERREO);
        }
        /**
         * 限流算法：使用了计数器  每个用户在一段时间内只能访问固定的次数
         */



        boolean check=orderService.checkCaptcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROE_CAPTCHA);
        }
        String str=orderService.createPath(user,goodsId);
        return RespBean.success(str);

    }


    @RequestMapping(value="/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if(user==null||goodsId<0){
            //请求非法，用户没有输入信息
            throw new GlobalException(RespBeanEnum.REQUESE_ILLEGAL);
        }
        //设置输出为图片类型
        response.setContentType("image/jpg");
        //设置验证码不缓存
        response.setHeader("Pargam","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        //将验证码的数字存放到redis中并且设置超时时间为五分钟
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),300, TimeUnit.SECONDS);

        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败"+e.getMessage());
        }


    }





}
