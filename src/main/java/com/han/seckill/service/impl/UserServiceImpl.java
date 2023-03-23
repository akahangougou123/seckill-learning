package com.han.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.han.seckill.exception.GlobalException;
import com.han.seckill.mapper.UserMapper;
import com.han.seckill.pojo.User;
import com.han.seckill.service.IUserService;
import com.han.seckill.utils.CookieUtil;
import com.han.seckill.utils.MD5Util;
import com.han.seckill.utils.UUIDUtil;
import com.han.seckill.vo.LoginVo;
import com.han.seckill.vo.RespBean;
import com.han.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    //登录
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        //根据手机号获取用户
        User user = userMapper.selectUserById(mobile);
//        System.out.println("user"+user);
        if (user == null){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }


        //判断密码是否正确
        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
//        1.用户信息存在session => 2.用户信息存在redis key:ticket value:user
//        request.getSession().setAttribute(ticket,user);
        redisTemplate.opsForValue().set("user:"+ticket,user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(user);
    }

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @param request
     * @param response
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User)redisTemplate.opsForValue().get("user:" + userTicket );
        if(user != null){
            CookieUtil.setCookie(request, response, "user", userTicket);
        }
        return user;
    }

    /**
     * 更新密码
     * @param request
     * @param response
     * @param userTicket
     * @param password
     * @return
     */
    @Override
    public RespBean updatePassword(HttpServletRequest request, HttpServletResponse response, String userTicket, String password) {
        User user = getUserByCookie(userTicket, request, response);
        if (user==null){
            throw new GlobalException(RespBeanEnum.MOBIE_NOT_EXIT);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if(result == 1){
            //删除redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL );
    }

    /**
     *  用户注册
     * @param loginVo
     * */
    @Override
    public RespBean doRegister(LoginVo loginVo){
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //查用户是否已经注册
        User user = userMapper.selectUserById(mobile);
        if(user != null){
            throw new GlobalException(RespBeanEnum.USER_IS_EXIT);
        }
        //将密码二次加密后插入数据库
        String dbPass = MD5Util.formPassToDBPass(formPass,"1a2b3c4d");
        loginVo.setPassword(dbPass);
        int result = userMapper.addUser(loginVo);
        //插入是否成功
        if(result != 1){
            throw new GlobalException(RespBeanEnum.ERROR);
        }
        log.info("用户注册成功{}",loginVo);
        return RespBean.success();
    }
}
