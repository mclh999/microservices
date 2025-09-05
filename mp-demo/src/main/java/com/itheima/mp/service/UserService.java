package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

public interface UserService extends IService<User> {
    void deduction(Long id, Integer money);

    List<User> queryUsers(UserQuery userQuery);

    UserVO queryUserByid(Long id);

    List<UserVO> queryUsersByIds(List<Long> ids);

    PageDTO<UserVO> queryUsersPage(UserQuery userQuery);
}
