package com.han.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.han.seckill.pojo.Goods;
import com.han.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);

}
