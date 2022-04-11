package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOptsException;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.UserMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author passion
 * @since 2022-04-02
 */
@SuppressWarnings("all")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    //注入userMapper
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        if(mobile.isEmpty()||password.isEmpty()){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
//        System.out.println("mobile is "+mobile);
        //查询出来的就是用户二次加密的密码
        User user = userMapper.selectById(mobile);

        if(null==user){
            throw new GlobalException(RespBeanEnum.LOGIN2_ERROR);
            //return RespBean.error(RespBeanEnum.LOGIN2_ERROR);
        }
        if(!MD5Util.fromPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN2_ERROR);
            //return RespBean.error(RespBeanEnum.LOGIN2_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        System.out.println("------------------------ticket  是 "+ticket);
        //将cookie存到session
        //request.getSession().setAttribute(ticket,user);

        /*
        使用redis存储数据  cookie
        opsForValue是写的string类型
         */

        redisTemplate.opsForValue().set("user:"+ticket,user);
        System.out.println(redisTemplate);

        CookieUtil.setCookie(request,response,"userTicket",ticket);

        return RespBean.success(ticket);

    }

    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(userTicket.isEmpty()){
            return null;
        }
        //从存放session的数据库中找到对应的user
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }


    @Override
    public RespBean updatePassword(String userTicket, Long id, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        //数据库更新密码
        int result=userMapper.updateById(user);
        if(1==result){
            //redis更新密码
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }

        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }


}
