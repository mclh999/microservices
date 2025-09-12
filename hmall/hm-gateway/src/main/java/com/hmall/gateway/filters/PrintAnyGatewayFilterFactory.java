//package com.hmall.gateway.filters;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class PrintAnyGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
//    //只有配置了才能生效
//    @Override
//    public GatewayFilter apply(Object config) {
////        return new GatewayFilter() {
////            @Override
////            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
////                //执行过滤器业务逻辑
////                System.out.println("执行过滤器业务逻辑");
////                //放行
////                return chain.filter(exchange);
////            }
////        };
//
//        //采用装饰类来创建过滤器，增加指定过滤器执行顺序功能
//        return new OrderedGatewayFilter(new GatewayFilter() {
//            @Override
//            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//                //执行过滤器业务逻辑
//                System.out.println("执行过滤器业务逻辑");
//                //放行
//                return chain.filter(exchange);
//            }
//        }, 0);
//    }
//}
