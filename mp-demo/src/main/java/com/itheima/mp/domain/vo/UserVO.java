package com.itheima.mp.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.enums.Userstatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "用户VO实体")
public class UserVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    //因为这里的VO是起到中转站的作用，不与表直接交互，所以不需要使用@TableField
    @ApiModelProperty("详细信息")
    private UserInfo info;

    @ApiModelProperty("使用状态（1正常 2冻结）")
    private Userstatus status;

    @ApiModelProperty("账户余额")
    private Integer balance;

    @ApiModelProperty("收货地址")
    private List<AddressVO> address;
}
