package com.zjgsu.lll.course2_new.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器 - 用于验证服务实例信息和负载均衡
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 返回当前服务实例信息
     * 用于验证负载均衡是否生效
     *
     * 当启动多个 catalog-service 实例时，
     * 通过这个接口可以看到请求被分配到不同的实例
     */
    @GetMapping("/instance")
    public Map<String, Object> getInstance() {
        Map<String, Object> result = new HashMap<>();
        result.put("service", serviceName);
        result.put("port", port);
        result.put("message", "This is " + serviceName + " running on port " + port);
        result.put("timestamp", System.currentTimeMillis());

        // 添加一些额外信息便于调试
        result.put("hostname", System.getenv("HOSTNAME"));

        return result;
    }

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", serviceName);
        result.put("port", port);
        return result;
    }
}