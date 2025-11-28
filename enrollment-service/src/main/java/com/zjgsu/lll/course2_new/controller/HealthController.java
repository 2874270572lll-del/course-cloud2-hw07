package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db")
    public ApiResponse<Map<String, Object>> checkDatabase() {
        Map<String, Object> healthInfo = new HashMap<>();

        try {
            // 执行简单查询测试数据库连接
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            // 获取表的记录数
            Long courseCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM courses", Long.class);
            Long studentCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM students", Long.class);
            Long enrollmentCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM enrollments", Long.class);

            healthInfo.put("status", "UP");
            healthInfo.put("database", "Connected");
            healthInfo.put("courseCount", courseCount);
            healthInfo.put("studentCount", studentCount);
            healthInfo.put("enrollmentCount", enrollmentCount);

            return ApiResponse.success(healthInfo);

        } catch (Exception e) {
            healthInfo.put("status", "DOWN");
            healthInfo.put("database", "Connection failed");
            healthInfo.put("error", e.getMessage());

            return ApiResponse.error(500, "Database health check failed");
        }
    }
}