package com.itheima.mp.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 专门用来接收用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")//指定了有参构造的静态方法名为of
public class UserInfo {

    private Integer age;
    private String intro;
    private String gender;
}
