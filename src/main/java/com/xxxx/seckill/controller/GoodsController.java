package com.xxxx.seckill.controller;


import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.service.impl.UserServiceImpl;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


    /**
     * 功能描述：跳转到商品列表页面  1.0版本  主要是简单的实现逻辑
     * @param
     * @param model
     * @param ticket
     * @return
     */
//    @RequestMapping("/toList")
//    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userTicket" )String ticket){
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        //通过session来获取用户
//        //User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
//        if(null==user){
//            return "login";
//        }
//        model.addAttribute("user",user);
//        return "goodsList";
//    }

    /**
     * 功能描述：跳转到商品列表页面  2.0版本  通过webConfig实现拦截功能 优化的版本
     * @param model
     * @param user
     * @return
     */

//    @RequestMapping("/toList")
//    public String toList(Model model,User user){
//        model.addAttribute("user",user);
//
//        model.addAttribute("goodsList",goodsService.findGoodsVo());
//        return "goodsList";
//
//    }

    /**
     * 功能描述: 缓存商品列表到redis   3.0版本使用redis作为缓存，缓存页面
     * @param model
     * @param user
     * @return
     */

    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,
                         HttpServletRequest request,HttpServletResponse response){

        //从redis中取出页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html= ((String) valueOperations.get("goodsList"));
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());


        html=thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);


        }
        return html;

    }


    /**
     * 使用redis作为页面缓存来优化 2.0
     * @param model
     * @param user
     * @param goodsId
     * @return
     */


//    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
//    @ResponseBody

//    public String toDetail(Model model, User user, @PathVariable Long goodsId,
//                            HttpServletRequest request,HttpServletResponse response){
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String html = (String)valueOperations.get("goodsDetail:" + goodsId);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
//
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        Date startDate = goodsVo.getStartDate();
//        Date endDate = goodsVo.getEndDate();
//        Date nowDate=new Date();
//
//        //秒杀的状态
//        int secKillStatus=0;
//        //秒杀倒计时
//        int remainSecond=0;
//
//        if(nowDate.before(startDate)){
//            /*
//            秒杀倒计时怎么做
//             */
//            remainSecond= (int) ((startDate.getTime()-nowDate.getTime())/1000);
//        }
//        else if(nowDate.after(endDate)){
//            //秒杀结束
//            secKillStatus=2;
//            remainSecond=-1;
//        }
//        else{
//            //秒杀开始
//            secKillStatus=1;
//            remainSecond=0;
//        }
//        model.addAttribute("remainSeconds",remainSecond);
//        model.addAttribute("secKillStatus",secKillStatus);
//        model.addAttribute("user",user);
//        model.addAttribute("goods",goodsVo);
//
//        //手动渲染
//        WebContext context=new WebContext(request,response,request.getServletContext(),request.getLocale(), model.asMap());
//
//        html=thymeleafViewResolver.getTemplateEngine().process("goodsDetail",context);
//
//        if(!StringUtils.isEmpty(html)){
//            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
//        }
//
//        return html;
//
//    }




    /**
     * 前后端分离  3.0
     * @param
     * @param user
     * @param goodsId

     * @return
     */

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId) {
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            //	秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }




}
