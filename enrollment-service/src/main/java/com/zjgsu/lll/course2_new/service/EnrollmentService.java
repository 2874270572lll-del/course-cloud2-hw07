package com.zjgsu.lll.course2_new.service;

import com.zjgsu.lll.course2_new.exception.*;
import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.EnrollmentStatus;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.repository.EnrollmentRepository;
import com.zjgsu.lll.course2_new.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j  // Lombok 日志
@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;  // 已通过 @LoadBalanced 配置
    private final DiscoveryClient discoveryClient;

    /**
     * ⭐ 关键改动：使用服务名而不是硬编码 URL
     * RestTemplate 已添加 @LoadBalanced，会自动通过 Nacos 解析服务名并负载均衡
     */
    private static final String CATALOG_SERVICE_NAME = "catalog-service";
    private static final String CATALOG_SERVICE_URL = "http://" + CATALOG_SERVICE_NAME;

    // 选课（文档要求：POST /api/enrollments）
    @Transactional
    public Enrollment enroll(String courseId, String studentId) {
        log.info("🎓 开始选课：courseId={}, studentId={}", courseId, studentId);

        // 1. 验证学生存在
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));

        // 2. ⭐ 通过服务名调用 catalog-service（自动负载均衡）
        String courseUrl = CATALOG_SERVICE_URL + "/api/courses/" + courseId;
        log.info("🔍 调用 catalog-service: {}", courseUrl);

        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(courseUrl, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Course", courseId);
        } catch (Exception e) {
            log.error("❌ 调用 catalog-service 失败: {}", e.getMessage());
            throw new ServiceUnavailableException("Catalog service is not available: " + e.getMessage());
        }

        // 3. 提取课程数据
        Map<String, Object> courseData = (Map<String, Object>) courseResponse.get("data");
        if (courseData == null) {
            throw new BusinessException("Invalid course response");
        }

        Integer capacity = (Integer) courseData.get("capacity");
        Integer enrolled = (Integer) courseData.get("enrolled");

        // 4. 校验课程容量
        if (enrolled >= capacity) {
            throw new BusinessException("Course is full");
        }

        // 5. 校验重复选课
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new BusinessException("Already enrolled in this course");
        }

        // 6. 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        Enrollment saved = enrollmentRepository.save(enrollment);

        // 7. 更新课程已选人数
        updateCourseEnrolledCount(courseId, enrolled + 1);

        log.info("✅ 选课成功：enrollmentId={}", saved.getId());
        return saved;
    }

    // 退课（文档要求：DELETE /api/enrollments/{id}）
    @Transactional
    public void dropEnrollment(String enrollmentId) {
        log.info("📤 开始退课：enrollmentId={}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new BusinessException("Enrollment is not active");
        }

        // 更新选课状态
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);

        // ⭐ 通过服务名更新课程人数
        try {
            String courseUrl = CATALOG_SERVICE_URL + "/api/courses/" + enrollment.getCourseId();
            Map<String, Object> courseResponse = restTemplate.getForObject(courseUrl, Map.class);
            Map<String, Object> courseData = (Map<String, Object>) courseResponse.get("data");
            Integer enrolled = (Integer) courseData.get("enrolled");
            updateCourseEnrolledCount(enrollment.getCourseId(), Math.max(0, enrolled - 1));
        } catch (Exception e) {
            log.error("❌ 更新课程人数失败（退课）: {}", e.getMessage());
        }

        log.info("✅ 退课成功");
    }

    // 工具方法：更新课程已选人数
    private void updateCourseEnrolledCount(String courseId, int newCount) {
        String url = CATALOG_SERVICE_URL + "/api/courses/" + courseId;
        Map<String, Object> updateData = Map.of("enrolled", newCount);
        try {
            restTemplate.put(url, updateData);
            log.info("✅ 更新课程人数成功：courseId={}, newCount={}", courseId, newCount);
        } catch (Exception e) {
            log.error("❌ 更新课程人数失败: {}", e.getMessage());
            // 仅记录日志，不影响主流程（最终一致性）
        }
    }

    /**
     * ⭐ 新增：获取 catalog-service 的所有可用实例
     * 用于验证服务发现和负载均衡
     */
    public List<ServiceInstance> getCatalogServiceInstances() {
        return discoveryClient.getInstances(CATALOG_SERVICE_NAME);
    }

    // 按课程查询选课记录
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    // 按学生查询选课记录
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // 获取所有选课记录
    @Transactional(readOnly = true)
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}