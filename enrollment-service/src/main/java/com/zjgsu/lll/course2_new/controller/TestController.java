package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试控制器 - 用于验证服务发现和负载均衡
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String serviceName;

    private final RestTemplate restTemplate;
    private final EnrollmentService enrollmentService;

    /**
     * 返回当前服务实例信息
     * 用于验证负载均衡是否生效
     */
    @GetMapping("/instance")
    public Map<String, Object> getInstance() {
        Map<String, Object> result = new HashMap<>();
        result.put("service", serviceName);
        result.put("port", port);
        result.put("message", "This is " + serviceName + " running on port " + port);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 测试调用 catalog-service
     * 用于验证服务发现是否正常工作
     */
    @GetMapping("/call-catalog")
    public Map<String, Object> callCatalogService() {
        Map<String, Object> result = new HashMap<>();

        try {
            // ⭐ 通过服务名调用（自动负载均衡）
            String url = "http://catalog-service/api/test/instance";
            Map<String, Object> catalogResponse = restTemplate.getForObject(url, Map.class);

            result.put("success", true);
            result.put("enrollment-service-port", port);
            result.put("catalog-service-response", catalogResponse);
            result.put("message", "✅ Successfully called catalog-service via service discovery!");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("message", "❌ Failed to call catalog-service");
        }

        return result;
    }

    /**
     * 获取 catalog-service 的所有实例
     * 用于验证 Nacos 服务发现
     */
    @GetMapping("/catalog-instances")
    public Map<String, Object> getCatalogInstances() {
        Map<String, Object> result = new HashMap<>();

        try {
            List<ServiceInstance> instances = enrollmentService.getCatalogServiceInstances();

            List<Map<String, String>> instanceList = instances.stream()
                    .map(instance -> {
                        Map<String, String> info = new HashMap<>();
                        info.put("instanceId", instance.getInstanceId());
                        info.put("host", instance.getHost());
                        info.put("port", String.valueOf(instance.getPort()));
                        info.put("uri", instance.getUri().toString());
                        return info;
                    })
                    .collect(Collectors.toList());

            result.put("success", true);
            result.put("service", "catalog-service");
            result.put("instanceCount", instances.size());
            result.put("instances", instanceList);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }


}