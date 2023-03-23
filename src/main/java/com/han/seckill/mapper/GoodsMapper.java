package com.han.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.han.seckill.pojo.Goods;
import com.han.seckill.pojo.Page;
import com.han.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(String goodsId);

    List<GoodsVo> queryAllGoods(@Param("pages") int pages, @Param("pageSize") int pageSize, int type);

    List<Goods> getDetailImg(String goodsId);

    List<GoodsVo> queryGoods(Page page);
}
