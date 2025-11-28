package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import com.zjgsu.lll.course2_new.model.Course;
import com.zjgsu.lll.course2_new.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;

    // 构造器注入依赖，符合Spring最佳实践
    public CourseController(CourseService service) {
        this.service = service;
    }

    // 1. 获取所有课程（适配文档要求：GET /api/courses）
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = service.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    // 2. 获取单个课程（适配文档要求：GET /api/courses/{id}）
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable String id) {
        Course course = service.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    // 3. 按课程代码查询课程（补充文档要求：GET /api/courses/code/{code}）
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<Course>> getCourseByCode(@PathVariable String code) {
        Course course = service.getCourseByCode(code);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    // 4. 创建课程（适配文档要求：POST /api/courses，返回201创建成功状态码）
    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        Course createdCourse = service.createCourse(course);
        return new ResponseEntity<>(ApiResponse.success(createdCourse), HttpStatus.CREATED);
    }

    // 5. 更新课程（适配文档要求：PUT /api/courses/{id}）
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @PathVariable String id,
            @RequestBody Course course
    ) {
        Course updatedCourse = service.updateCourse(id, course);
        return ResponseEntity.ok(ApiResponse.success(updatedCourse));
    }

    // 6. 删除课程（适配文档要求：DELETE /api/courses/{id}）
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable String id) {
        service.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 7. 扩展：获取可用课程（可选，适配CourseService的getAvailableCourses方法，返回有剩余容量的课程）
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Course>>> getAvailableCourses() {
        List<Course> availableCourses = service.getAvailableCourses();
        return ResponseEntity.ok(ApiResponse.success(availableCourses));
    }


}