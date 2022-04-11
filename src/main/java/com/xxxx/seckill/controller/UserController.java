package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSenderTest;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author passion
 * @since 2022-04-02
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSenderTest mqSender;

    /**
     * 功能描述：用户信息测试
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){

        return RespBean.success(user);
    }

//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello");
//
//    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq01(){
        mqSender.send01("hello");

    }

    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq02(){
        mqSender.send02("你好");
    }

    @RequestMapping("/mq/direct03")
    @ResponseBody
    public void mq03(){
        mqSender.send03("你好");
    }


}
