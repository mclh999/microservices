package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
//@AllArgsConstructor, 不需要所有的变量都初始化
@RequiredArgsConstructor//只初始化必要的变量，可以通过final来选择要初始的变量
public class UserController {
//    @Autowired,不推荐使用这个
    private final UserService userService;

    @PostMapping
    public void save(@RequestBody UserFormDTO userFormDTO) {
        //胡图包将DTO转换成VO
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        //调用新增方法
        userService.save(user);
    }

}
