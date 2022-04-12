package com.xxxx.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum RespBeanEnum {

    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务器异常"),

    //登录模块
    LOGIN_ERROR(500210,"用户名或者密码不能为空"),
    MOBILE_ERROR(500212,"手机号格式不正确"),
    MOBILE_NOT_EXIST(500213,"手机号码不存在"),

    LOGIN2_ERROR(5000211,"用户名或者密码有错误"),
    //绑定参数异常
    BIND_ERROR(500213,"参数绑定异常"),

    //更新密码
    PASSWORD_UPDATE_FAIL(500214,"密码更新错误"),

    SESSION_ERREO(500215,"用户不存在"),


    //秒杀模块
    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500501,"该商品每人限购一件"),

    //商品模块 5003xxx
    ORDER_NOT_EXIST(500300,"订单不存在"),

    //秒杀的路径不对
    REQUESE_ILLEGAL(500301,"请求非法,请重新尝试"),

    //验证码错误
    ERROE_CAPTCHA(500302,"验证码错误");


    private final Integer code;
    private final String message;

}
