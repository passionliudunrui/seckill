package com.xxxx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author passion
 * @since 2022-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long userId;

    private Long goodsId;

    private Long deliveryAddrId;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsPrice;

    private Integer orderChannel;

    private Integer status;

    private Date createDate;

    private Date payDate;


}
