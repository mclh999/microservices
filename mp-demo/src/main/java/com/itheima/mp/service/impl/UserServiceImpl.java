package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.Userstatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        if(user.getStatus()==Userstatus.LOCKED || user == null){
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
                .set(remainBalance==0,User::getStatus,Userstatus.LOCKED)
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

    /**
     * 根据id查询用户,包含地址
     * @param id
     * @return
     */
    @Override
    public UserVO queryUserByid(Long id) {
        //查询用户
        User user = this.getById(id);

        //判断用户状态
        if (user.getStatus() != Userstatus.NORMAL || user == null) {
            throw new RuntimeException("用户不存在或者被禁用");
        }

        //查询地址
        List<Address> addressList = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();

        //封装成VO
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);

        //用糊涂包判断是否为空
        if(CollUtil.isNotEmpty(addressList)){
            userVO.setAddress(BeanUtil.copyToList(addressList, AddressVO.class));
        }

        return userVO;

    }

    /**
     * 根据id批量查询用户
     * @param ids
     * @return
     */
    @Override
    public List<UserVO> queryUsersByIds(List<Long> ids) {
        //解法一：
//        //创建收集UserVO集合
//        List<UserVO> userVOList = new ArrayList<>();
//
//        for (Long id : ids) {
//            //查询用户
//            User user = this.getById(id);
//
//            //判断用户状态
//            if (user.getStatus() != 1 || user == null) {
//                throw new RuntimeException("用户不存在或者被禁用");
//            }
//
//            //查询地址
//            List<Address> addressList = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();
//
//            //封装成VO
//            UserVO userVO = new UserVO();
//            BeanUtil.copyProperties(user, userVO);
//
//            //用糊涂包判断是否为空
//            if(CollUtil.isNotEmpty(addressList)){
//                userVO.setAddress(BeanUtil.copyToList(addressList, AddressVO.class));
//                userVOList.add(userVO);
//            }
//        }
//        return userVOList;

        //解法二,提高性能，减少数据库查询的次数
        //查询用户
        List<User> userList = listByIds(ids);
        if(CollUtil.isEmpty(userList)){
            return Collections.emptyList();
        }

        //查询用户地址
        //获取用户ID集合
        List<Long> userIds = userList.stream().map(User::getId).collect(Collectors.toList());
        //根据ID查询地址
        List<Address> addressList = Db.lambdaQuery(Address.class).in(Address::getUserId, userIds).list();
        //转换成VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addressList, AddressVO.class);
        //将用户地址集合分组处理，相同用户的放入一个集合组中
        Map<Long, List<AddressVO>> addressMap = new HashMap<>();
        if(CollUtil.isNotEmpty(addressVOList)){
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        //转换成VO返回
        List<UserVO> userVOList = new ArrayList<>(userList.size());
        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user,userVO);

            userVO.setAddress(addressMap.get(user.getId()));
            userVOList.add(userVO);//userVOList记录的是VO的地址，所以后续VO的修改都会影响到userVOList里面的数据
        }
        return userVOList;

    }

    /**
     * 分页查询用户
     * @param userQuery
     * @return
     */
    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery userQuery) {
        //构建查询条件
        Page<User> page = userQuery.toMpPageDefaultSortByUpdateTimeDesc();

        //分页查询
        Page<User> p = lambdaQuery()
                .like(userQuery.getName() != null, User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())//这里是查询条件
                .page(page);

        //封装VO结果并返回,这里需要保证VO和PO的字段名保持一致
//        return PageDTO.of(p, UserVO.class);
        //简单处理
//        return PageDTO.of(p, user->BeanUtil.copyProperties(user,UserVO.class));
        //复杂处理
        return PageDTO.of(p,user -> {
            //基础属性拷贝
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            //额外处理
            userVO.setUsername(user.getUsername().toString().substring(0,user.getUsername().length()-2)+"**");
            return userVO;
        });

    }
}
