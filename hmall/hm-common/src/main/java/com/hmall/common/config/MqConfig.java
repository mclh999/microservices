package com.hmall.common.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RabbitTemplate.class)//只有使用了RabbitTemplate类时才生效
public class MqConfig {

    @Bean//记得在配置文件里面声明来使其生效
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
