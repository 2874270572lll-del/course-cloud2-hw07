package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.service.EnrollmentService;
import com.zjgsu.lll.course2_new.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Enrollment>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllEnrollments()));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> byCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(ApiResponse.success(service.getEnrollmentsByCourse(courseId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> byStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(ApiResponse.success(service.getEnrollmentsByStudent(studentId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> enroll(@RequestBody Map<String, String> req) {
        // 接收courseId和studentId参数
        Enrollment enrollment = service.enroll(req.get("courseId"), req.get("studentId"));
        return new ResponseEntity<>(ApiResponse.success(enrollment), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> drop(@PathVariable String id) {
        service.dropEnrollment(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}