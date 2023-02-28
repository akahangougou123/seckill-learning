package com.han.seckill.controller;


import com.han.seckill.pojo.User;
import com.han.seckill.service.IOrderService;
import com.han.seckill.vo.OrderDetailVo;
import com.han.seckill.vo.RespBean;
import com.han.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 功能描述：返回订单详情
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId){
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return  RespBean.success(detail);
    }
}
