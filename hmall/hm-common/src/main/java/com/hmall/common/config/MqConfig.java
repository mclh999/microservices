package com.hmall.common.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Bean//记得在配置文件里面声明来使其生效
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
