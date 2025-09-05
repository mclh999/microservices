package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.removeById(id);
    }

    @GetMapping("/{id}")
    public UserVO get(@PathVariable Long id){
        //包含了地址
        return userService.queryUserByid(id);
    }

    @GetMapping
    @ApiOperation("批量查询")
    public List<UserVO> list(@RequestParam("ids") List<Long>ids){
        return userService.queryUsersByIds(ids);
    }

    @PutMapping("/{id}/deduction/{money}")
    public void deduction(@PathVariable Long id, @PathVariable Integer money){
        userService.deduction(id,money);
    }

    @GetMapping("/list")//记得修改成不同的，否则会报错
    @ApiOperation("根据复杂条件批量查询")
    public List<UserVO> query(UserQuery userQuery){//因为是get方式请求，不用json格式的数据，所以参数不能用@RequestBody
        List<User> userList = userService.queryUsers(userQuery);
        return BeanUtil.copyToList(userList, UserVO.class);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public PageDTO<UserVO> page(UserQuery userQuery){
        return userService.queryUsersPage(userQuery);
    }



}
