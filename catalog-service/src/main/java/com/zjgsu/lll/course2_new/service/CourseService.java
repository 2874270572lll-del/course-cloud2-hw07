package com.zjgsu.lll.course2_new.service;

import com.zjgsu.lll.course2_new.model.Course;
import com.zjgsu.lll.course2_new.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    // 构造器注入，符合Spring推荐依赖注入方式
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // 获取所有课程（适配文档GET /api/courses接口）
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 获取单个课程（按ID，适配文档GET /api/courses/{id}接口）
    @Transactional(readOnly = true)
    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    // 按课程代码查询（适配文档GET /api/courses/code/{code}接口）
    @Transactional(readOnly = true)
    public Course getCourseByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Course not found with code: " + code));
    }

    // 创建课程（适配文档POST /api/courses接口，补充业务校验）
    public Course createCourse(Course course) {
        // 1. 校验课程代码唯一性
        if (course.getCode() != null && courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("Course code already exists: " + course.getCode());
        }

        // 2. 自动生成ID（避免手动传参遗漏）
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId(UUID.randomUUID().toString());
        }

        // 3. 校验课程容量合法性（必须大于0）
        if (course.getCapacity() <= 0) {
            throw new RuntimeException("Course capacity must be greater than 0");
        }

        // 4. 初始化已选人数（默认0，防止创建时未传值导致异常）
        if (course.getEnrolled() < 0) {
            throw new RuntimeException("Enrolled count cannot be negative");
        }
        if (course.getEnrolled() == 0) {
            course.setEnrolled(0);
        }

        return courseRepository.save(course);
    }

    // 更新课程（适配文档PUT /api/courses/{id}接口，支持已选人数更新）
    public Course updateCourse(String id, Course updated) {
        // 1. 先查询原课程是否存在
        Course existing = getCourseById(id);

        // 2. 保留不可变更字段（ID、创建时间）
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());

        // 3. 若课程代码变更，校验新代码唯一性
        if (!existing.getCode().equals(updated.getCode()) &&
                courseRepository.existsByCode(updated.getCode())) {
            throw new RuntimeException("Course code already exists: " + updated.getCode());
        }

        // 4. 校验容量合法性：新容量不能小于当前已选人数
        if (updated.getCapacity() < existing.getEnrolled()) {
            throw new RuntimeException("Capacity cannot be less than enrolled count (current enrolled: " + existing.getEnrolled() + ")");
        }

        // 5. 校验已选人数合法性（更新时不可为负）
        if (updated.getEnrolled() < 0) {
            throw new RuntimeException("Enrolled count cannot be negative");
        }

        return courseRepository.save(updated);
    }

    // 删除课程（适配文档DELETE /api/courses/{id}接口）
    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    // 获取可用课程（扩展功能，需确保CourseRepository存在findAvailableCourses方法）
    @Transactional(readOnly = true)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }
}