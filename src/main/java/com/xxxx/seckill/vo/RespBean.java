package com.xxxx.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关于返回的结果集
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RespBean {
    private long code;
    private String message;
    private  Object obj;

    public static RespBean success(){
        System.out.println(RespBeanEnum.SUCCESS.getMessage());
//        System.out.println(RespBean.success().getMessage());
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }

    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),obj);

    }


    public static RespBean error(RespBeanEnum respBeanEnum){

        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), obj);
    }



//    public static void main(String[] args) {
//        RespBean respBean=new RespBean();
//        respBean.success();
//    }

}
