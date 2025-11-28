package com.zjgsu.lll.course2_new.service;

import com.zjgsu.lll.course2_new.exception.ResourceNotFoundException;
import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.EnrollmentStatus;
import com.zjgsu.lll.course2_new.model.Student;
import com.zjgsu.lll.course2_new.repository.EnrollmentRepository;
import com.zjgsu.lll.course2_new.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    // 获取所有学生（文档要求：GET /api/students）
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 按ID获取学生（文档要求：GET /api/students/{id}）
    @Transactional(readOnly = true)
    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    // 创建学生（文档要求：POST /api/students）
    @Transactional
    public Student createStudent(Student student) {
        if (student.getId() == null) {
            student.setId(UUID.randomUUID().toString());
        }
        // 校验学号唯一性
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("Student ID already exists: " + student.getStudentId());
        }
        return studentRepository.save(student);
    }

    // 更新学生（文档要求：PUT /api/students/{id}）
    @Transactional
    public Student updateStudent(String id, Student student) {
        Student existing = getStudentById(id);
        existing.setName(student.getName());
        existing.setMajor(student.getMajor());
        existing.setGrade(student.getGrade());
        existing.setEmail(student.getEmail());
        return studentRepository.save(existing);
    }

    // 删除学生（文档要求：DELETE /api/students/{id}）
    @Transactional
    public void deleteStudent(String id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
}