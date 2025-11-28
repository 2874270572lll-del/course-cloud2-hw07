package com.zjgsu.lll.course2_new.repository;

import com.zjgsu.lll.course2_new.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    // 按课程代码查询（适配文档GET /api/courses/code/{code}接口，支持课程唯一性校验）
    Optional<Course> findByCode(String code);

    // 检查课程代码是否存在（适配CourseService中创建/更新课程时的代码唯一性校验逻辑）
    boolean existsByCode(String code);

    // 按讲师ID查询课程（扩展功能，支持按讲师筛选课程，适配后续可能的讲师相关业务）
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findByInstructorId(@Param("instructorId") String instructorId);

    // 查询有剩余容量的课程（适配CourseService的getAvailableCourses方法，返回可选课的课程列表）
    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findAvailableCourses();

    // 按标题关键字模糊查询（适配CourseService的searchByTitle方法，支持课程标题搜索）
    // 补充IgnoreCase，实现不区分大小写搜索，提升用户体验
    List<Course> findByTitleContainingIgnoreCase(String keyword);

    // 统计有剩余容量的课程数量（扩展功能，支持统计可用课程总数，适配数据看板等场景）
    @Query("SELECT COUNT(c) FROM Course c WHERE c.enrolled < c.capacity")
    long countAvailableCourses();
}