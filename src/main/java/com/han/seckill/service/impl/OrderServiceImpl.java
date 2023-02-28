package com.han.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.han.seckill.exception.GlobalException;
import com.han.seckill.mapper.OrderMapper;
import com.han.seckill.pojo.Order;
import com.han.seckill.pojo.SeckillGoods;
import com.han.seckill.pojo.SeckillOrder;
import com.han.seckill.pojo.User;
import com.han.seckill.service.IGoodsService;
import com.han.seckill.service.IOrderService;
import com.han.seckill.service.ISeckillGoodsService;
import com.han.seckill.service.ISeckillOrderService;
import com.han.seckill.vo.GoodsVo;
import com.han.seckill.vo.OrderDetailVo;
import com.han.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    //private IKillGoodsService secKillGoodsService;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    //private IKillOrderService secKillOrderService;
    @Autowired
    private IGoodsService goodsService;
    /**
     * 秒杀商品
     * @param user
     * @param goods
     * @return
     */
    @Override
    public Order seckill(User user, GoodsVo goods) {
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsService.updateById(seckillGoods);
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
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        //xxxxService.save()调用的就是xxxxMapper().insert()方法
        seckillOrderService.save(seckillOrder);

        return order;
    }

    /**
     * 功能描述:返回订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId == null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIT);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodsVo);
        return orderDetailVo;
    }
}
