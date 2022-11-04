package com.han.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.han.seckill.pojo.Order;
import com.han.seckill.pojo.User;
import com.han.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-10-22
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
