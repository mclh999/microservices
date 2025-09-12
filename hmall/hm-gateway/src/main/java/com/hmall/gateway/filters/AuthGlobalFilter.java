package com.hmall.gateway.filters;

import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    
    private final JwtTool jwtTool;

    //用于匹配路，要用spring自己的，这里直接new就省的再写一个方法然后加bean了
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求
        ServerHttpRequest request = exchange.getRequest();
        //判断是否需要进行登录校验
        if(isExist(request.getPath().toString())){
            //放行
            return chain.filter(exchange);
        }

        //获取token
        List<String> headers = request.getHeaders().get("authorization");
        String token = null;
        if(headers!=null && !headers.isEmpty()){
            token = headers.get(0);
        }

        //校验并解析token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        } catch (Exception e) {
            //拦截响应，设置响应码为401，提示用户未登录
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);//设置401
            return response.setComplete();//直接返回，中止请求
        }

        //传递用户信息
        String userInfo = userId.toString();
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();

        //放行
        return chain.filter(swe);
    }

    private boolean isExist(String path) {
        for (String pathMach : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathMach, path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
