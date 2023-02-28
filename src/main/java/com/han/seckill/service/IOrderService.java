package com.han.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.han.seckill.pojo.Order;
import com.han.seckill.pojo.User;
import com.han.seckill.vo.GoodsVo;
import com.han.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
public interface IOrderService extends IService<Order> {
    /**
     * 功能描述: 秒杀商品
     * @param user
     * @param goods
     * @return
     */
    Order seckill(User user, GoodsVo goods);

    /**
     * 功能描述: 查询订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);
}
