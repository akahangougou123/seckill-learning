package com.han.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.han.seckill.pojo.Order;
import com.han.seckill.pojo.SeckillOrder;
import com.han.seckill.pojo.User;
import com.han.seckill.service.IGoodsService;
import com.han.seckill.service.IOrderService;
import com.han.seckill.service.ISeckillOrderService;
import com.han.seckill.service.IUserService;
import com.han.seckill.vo.GoodsVo;
import com.han.seckill.vo.RespBean;
import com.han.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 秒杀请求控制器
 */

@RestController
@Slf4j
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 收到秒杀请求，先对请求的商品进行判断，判断通过再进行秒杀
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSecKill2")
    public String doSecKill2(Model model, User user, String goodsId){
        if(user == null){
            return "login";
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        log.info("{}",user);
        log.info("{}",goods);
        System.out.println("1111");
        //判断是否还有库存
        if(goods.getStockCount() < 1){
            System.out.println("2222");
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "goodsDetail";
        }
        //判断是否重复购买
        System.out.println("3333");
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        System.out.println("4444");
        if(seckillOrder != null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user, goods);
        log.info("{}", order);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);

        return "orderDetail";
    }

    @RequestMapping(value = "/doSecKill1",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(Model model, User user, String goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        log.info("用户:"+user.getId()+"秒杀商品:"+goodsId);

        //判断是否还有库存
        if(goods.getStockCount() < 1){
            model.addAttribute("error", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复购买
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder != null){
            model.addAttribute("error",RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user, goods);
        log.info("{}", order);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);

        return RespBean.success(order);
    }
    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    public RespBean doSeckill3(String userId,String goodsId){
        if(userId == null){
            return RespBean.error(RespBeanEnum.NOT_LOGIN);
        }
//        System.out.println("user:"+userId);
//        System.out.println("goodsId:"+goodsId);
        User user = userService.getById(userId);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        log.info("用户:"+user.getId()+"秒杀商品:"+goodsId);

        //判断是否还有库存
        if(goods.getStockCount() < 1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复购买
        //SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":"+goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user, goods);

        log.info("{}", order);
        if(order == null){
            return RespBean.error(RespBeanEnum.ERROR);
        }
        return RespBean.success(order);
    }
}
