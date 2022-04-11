package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author passion
 * @since 2022-04-02
 */
public interface IUserService extends IService<User> {


    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    //根据cookie获取用户
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    //修改用户的密码 （模拟用户信心的变更）
    RespBean updatePassword(String userTicket,Long id,String password,HttpServletRequest request, HttpServletResponse response);
}
