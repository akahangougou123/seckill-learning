package com.han.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.han.seckill.mapper.KillOrderMapper;
import com.han.seckill.mapper.OrderMapper;
import com.han.seckill.pojo.KillGoods;
import com.han.seckill.pojo.KillOrder;
import com.han.seckill.pojo.Order;
import com.han.seckill.pojo.User;
import com.han.seckill.service.IKillGoodsService;
import com.han.seckill.service.IKillOrderService;
import com.han.seckill.service.IOrderService;
import com.han.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-10-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IKillGoodsService secKillGoodsService;

   @Autowired
   private OrderMapper orderMapper;
   @Autowired
   private IKillOrderService secKillOrderService;

    /**
     * 秒杀商品
     * @param user
     * @param goods
     * @return
     */
    @Override
    public Order seckill(User user, GoodsVo goods) {
        //秒杀商品表减库存
        KillGoods killGoods = secKillGoodsService.getOne(new QueryWrapper<KillGoods>().eq("goods_id", goods.getId()));
        killGoods.setStockCount(killGoods.getStockCount() - 1);
        secKillGoodsService.updateById(killGoods);
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setGoodsCount(1);
        order.setOrderChannel(1);
        order.setDeliveryAddrId(0L);
        order.setCreateDate(new Date());
        order.setStatus(0);
        order.setPayDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        KillOrder killOrder = new KillOrder();
        killOrder.setUserId(user.getId());
        killOrder.setGoodsId(goods.getId());
        killOrder.setOrderId(order.getId());
        //xxxxService.save()调用的就是xxxxMapper().insert()方法
        secKillOrderService.save(killOrder);

        return order;
    }
}
