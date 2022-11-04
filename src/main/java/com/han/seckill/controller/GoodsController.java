package com.han.seckill.controller;

import com.han.seckill.pojo.User;
import com.han.seckill.service.IGoodsService;
import com.han.seckill.service.IUserService;
import com.han.seckill.vo.DetailVo;
import com.han.seckill.vo.GoodsVo;
import com.han.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;

    @RequestMapping("/toList")
    public String toList(Model model, User user){
//        System.out.println(ticket);
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        User user = (User) session.getAttribute(ticket);
//        User user =  userService.getUserByCookie(ticket, request, response);
//        System.out.println("user:"+user);
//        if(null == user){
//            return "login";
//        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodVo());
        return "goodsList";
    }

    /**
     * 跳转商品详情页
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){
//        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态 0:倒计时 1:进行中 2:已结束
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀未开始
        if(nowDate.before(startDate)){
            seckillStatus =  0;
            remainSeconds = ( (int) ((startDate.getTime()-nowDate.getTime())/1000) );
        } else if (nowDate.after(endDate)) {
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            seckillStatus = 1;
            remainSeconds = 1;
        }
        log.info("{}",seckillStatus);
//        model.addAttribute("seckillStatus",seckillStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
//        model.addAttribute("goods",goodsVo );
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
