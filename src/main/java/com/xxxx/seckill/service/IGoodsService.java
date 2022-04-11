package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.vo.GoodsVo;
import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author passion
 * @since 2022-04-04
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 功能描述：获取商品列表
     *
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 功能描述：获取商品详情
     * @param goodsId
     * @return
     */

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
