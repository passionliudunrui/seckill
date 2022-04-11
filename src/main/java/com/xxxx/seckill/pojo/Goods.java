package com.xxxx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("t_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private String goodsDetail;

    private BigDecimal goodsPrice;

    private Integer goodsStock;


}
