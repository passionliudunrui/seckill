package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
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
 * @since 2022-04-04
 */
@Controller
@RequestMapping("/order")

public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 订单详情剖析
     * @param user
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERREO);

        }

        OrderDetailVo detail=orderService.detail(orderId);

        return RespBean.success(detail);




    }

}
