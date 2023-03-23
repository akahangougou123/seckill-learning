package com.han.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.han.seckill.pojo.User;
import com.han.seckill.vo.LoginVo;
import com.han.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
public interface IUserService extends IService<User> {
    RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    RespBean doRegister(@Valid LoginVo loginVo);

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @param request
     * @param response
     * @return
     */
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);


    /**
     * 更新密码
     * @param request
     * @param response
     * @param userTicket
     * @param password
     * @return
     */
    RespBean updatePassword(HttpServletRequest request, HttpServletResponse response, String userTicket, String password);


}
