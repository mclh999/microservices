package com.hmall.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class dynamicRouteLoader {
    private final NacosConfigManager nacosConfigManager;
    private final RouteDefinitionWriter writer;

    //路由配置的文件的ID和分组
    private static final String dataId = "gateway-routes.json";
    private static final String group = "DEFAULT_GROUP";
    //保存更新的路由
    private final Set<String> updateRouteIds = new HashSet<>();

    @PostConstruct//启动时加载这个方法
    public void initRouteConfigListener() throws NacosException {
        //注册监听器并拉取配置
        String config = nacosConfigManager.getConfigService().getConfigAndSignListener(dataId, group, 5000, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String config) {
                updateConfigInfo(config);
            }
        });

        //首次启动时，更新一次配置
        updateConfigInfo(config);
    }

    public void updateConfigInfo(String configInfo) {
        log.debug("监听到路由配置,{}", configInfo);

        //解析配置信息，转换成RouteDefinition
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);

        //更新路由表
        //删除旧路由表
        for (String updateRouteId : updateRouteIds) {
            writer.delete(Mono.just(updateRouteId)).subscribe();//一定要加上后面的subscribe(订阅)
        }

        //添加新路由表
        for (RouteDefinition routeDefinition : routeDefinitions) {
            writer.save(Mono.just(routeDefinition)).subscribe();
            //保存更新的路由
            updateRouteIds.add(routeDefinition.getId());
        }

    }
}
