package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 传入UserMapper是为了调用对应的Mapper方法
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public void deduction(Long id, Integer money) {
        //查询用户
        User user = this.getById(id);
        //校验用户状态
        if(user.getStatus()==2 || user == null){
            throw new RuntimeException("用户不存在或者被禁用");
        }
        //校验余额是否充足
        if(user.getBalance()<money){
            throw new RuntimeException("余额不足");
        }
        //获取剩余余额
        int remainBalance = user.getBalance() - money;

        //更新用户余额,若余额为零就修改用户状态
//        baseMapper.updateBalanceById(id, money);
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance==0,User::getStatus,2)
                .eq(User::getId, id)
                .update();//上面的是编写SQL语句，这里的才是执行语句
    }

    /**
     * 根据复杂条件查询
     * @param userQuery
     * @return
     */
    @Override
    public List<User> queryUsers(UserQuery userQuery) {
        //通过lambdaQuery直接编写相应的sql语句并返回数据
        return lambdaQuery()
                .like(userQuery.getName()!=null,User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus()!=null,User::getStatus, userQuery.getStatus())
                .ge(userQuery.getMinBalance()!=null,User::getBalance, userQuery.getMinBalance())
                .le(userQuery.getMaxBalance()!=null,User::getBalance, userQuery.getMaxBalance())
                .list();
    }
}
