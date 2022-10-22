package com.han.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.han.seckill.pojo.Goods;
import com.han.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2022-10-22
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
}
