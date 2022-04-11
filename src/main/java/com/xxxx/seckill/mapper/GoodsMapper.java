package com.xxxx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author passion
 * @since 2022-04-04
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     * @return
     */

    List<GoodsVo> fingGoodsVo();


    //获取商品详情
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
