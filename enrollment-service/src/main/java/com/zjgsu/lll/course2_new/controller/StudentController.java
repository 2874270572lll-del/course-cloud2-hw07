package com.zjgsu.lll.course2_new.controller;

import com.zjgsu.lll.course2_new.common.ApiResponse;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllStudents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getStudentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Student>> create(@RequestBody Student student) {
        return new ResponseEntity<>(ApiResponse.success(service.createStudent(student)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> update(@PathVariable String id, @RequestBody Student student) {
        return ResponseEntity.ok(ApiResponse.success(service.updateStudent(id, student)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}