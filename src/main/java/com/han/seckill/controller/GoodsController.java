package com.han.seckill.controller;

import com.han.seckill.pojo.User;
import com.han.seckill.service.IGoodsService;
import com.han.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
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
}
