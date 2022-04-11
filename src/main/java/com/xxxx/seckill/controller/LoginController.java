package com.xxxx.seckill.controller;

import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
@Slf4j

public class LoginController {


    //自动注入service
    @Autowired
    private IUserService userService;


    /**
     * 功能描述： 跳转登录页面
     *
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){

        return "login";

    }

    /**
     * 功能描述：做用户登录时候的权限验证
     * @Valid是专门做校验的工具  添加了@Valid是准备对参数做权限验证
     * @param loginVo
     * @return
     */

    @RequestMapping("doLogin")
    @ResponseBody  //responseBody注解的意思是返回字符串

    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info("{}",loginVo);
        return userService.doLogin(loginVo,request,response);

    }


}
