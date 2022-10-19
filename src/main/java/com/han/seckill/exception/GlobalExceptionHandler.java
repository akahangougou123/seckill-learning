package com.han.seckill.exception;

import com.han.seckill.vo.RespBean;
import com.han.seckill.vo.RespBeanEnum;
import org.apache.tomcat.jni.Global;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        //  判断是否属于全局异常
        if(e instanceof GlobalException){
            //属于全局异常 强转为全局异常
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常："+ ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        return  RespBean.error(RespBeanEnum.ERROR);
    }
}
