package com.zjgsu.lll.course2_new.repository;

import com.zjgsu.lll.course2_new.model.Enrollment;
import com.zjgsu.lll.course2_new.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    List<Enrollment> findByCourseId(String courseId);

    List<Enrollment> findByStudentId(String studentId);

    Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    List<Enrollment> findByCourseIdAndStatus(String courseId, EnrollmentStatus status);

    List<Enrollment> findByStudentIdAndStatus(String studentId, EnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = :courseId AND e.status = 'ACTIVE'")
    long countActiveByCourseId(@Param("courseId") String courseId);

    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.courseId = :courseId " +
            "AND e.studentId = :studentId AND e.status = 'ACTIVE'")
    boolean existsByCourseIdAndStudentId(
            @Param("courseId") String courseId,
            @Param("studentId") String studentId
    );
}