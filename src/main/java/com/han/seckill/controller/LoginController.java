package com.han.seckill.controller;

import com.han.seckill.pojo.User;
import com.han.seckill.service.IUserService;
import com.han.seckill.vo.LoginVo;
import com.han.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;


    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/doLogin1",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doLogin1(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info("{}",loginVo);
        return userService.doLogin(loginVo, request, response);
    }

    @RequestMapping(value = "/doLogin",method = RequestMethod.POST)
    public RespBean doLogin(@RequestBody LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(loginVo);
        log.info("用户登录:{}", loginVo);
        return userService.doLogin(loginVo, request, response);
    }

    @RequestMapping(value = "/doRegister",method = RequestMethod.POST)
    public RespBean doRegister(@RequestBody LoginVo loginVo){

        log.info("用户注册{}",loginVo);
        return userService.doRegister(loginVo);
    }
}
