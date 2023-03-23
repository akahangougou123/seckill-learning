package com.han.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.han.seckill.pojo.User;
import com.han.seckill.vo.LoginVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
public interface UserMapper extends BaseMapper<User> {

    public User selectUserById(String Id);

    int addUser(LoginVo loginVo);
}
