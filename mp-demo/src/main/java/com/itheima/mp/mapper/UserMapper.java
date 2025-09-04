package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.Constants;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryUserByIds(@Param("ids") List<Long> ids);

    void updateBalanceByIds(@Param("ew")UpdateWrapper<User> updateWrapper,@Param("amount")int amount);

    //更新用户余额，因为是简单的业务逻辑，所以不需要写Wrapper
    void updateBalanceById(@Param("id") Long id, @Param("money") Integer money);
}
