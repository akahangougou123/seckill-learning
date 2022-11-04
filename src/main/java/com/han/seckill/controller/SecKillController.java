package com.han.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.han.seckill.pojo.KillOrder;
import com.han.seckill.pojo.Order;
import com.han.seckill.pojo.User;
import com.han.seckill.service.IGoodsService;
import com.han.seckill.service.IKillGoodsService;
import com.han.seckill.service.IKillOrderService;
import com.han.seckill.service.IOrderService;
import com.han.seckill.vo.GoodsVo;
import com.han.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLOutput;

@Controller
@Slf4j
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IKillOrderService secKillOrderService;

    @Autowired
    private IOrderService orderService;

    /**
     * 收到秒杀请求，先对请求的商品进行判断，判断通过再进行秒杀
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSecKill")
    public String doSecKill(Model model, User user, Long goodsId){
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
        KillOrder secKillOrder = secKillOrderService.getOne(new QueryWrapper<KillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        System.out.println("4444");
        if(secKillOrder != null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user, goods);
        log.info("{}", order);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);

        return "orderDetail";
    }
}
