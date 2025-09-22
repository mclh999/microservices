package com.hmall.api.config;

import com.hmall.api.client.fallback.CartClientFallback;
import com.hmall.api.client.fallback.ItemClientFallback;
import com.hmall.api.client.fallback.OrderClientFallback;
import com.hmall.api.client.fallback.UserClientFallback;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

/**
 * 日志配置,拦截器配置,要想生效，要让对应的微服务模块启动类配置启用此配置类
 */
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel (){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor (){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long userId = UserContext.getUser();
                if(userId != null){
                    requestTemplate.header("user-info",userId.toString());//将经过网关的微服务的userId传递给服务提供者

                }
            }
        };
    }

    @Bean//一般是写在一个独立的配置类里,记得绑定bean，否则无效
    public ItemClientFallback itemFallback(){
        return new ItemClientFallback();
    }

    @Bean
    public CartClientFallback cartFallback(){
        return new CartClientFallback();
    }

    @Bean
    public UserClientFallback userFallback(){
        return new UserClientFallback();
    }

    @Bean
    public OrderClientFallback orderFallback(){
        return new OrderClientFallback();
    }
}
