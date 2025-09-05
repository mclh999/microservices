package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Userstatus {
    NORMAL(1, "正常"),
    LOCKED(2, "锁定")
    ;

    @EnumValue
    @JsonValue//这个注解加到哪里就可以指定返回的枚举是哪个类型的，这里就会返回枚举的value，默认返回枚举的name
    private final Integer value;
    private final String desc;

    Userstatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
