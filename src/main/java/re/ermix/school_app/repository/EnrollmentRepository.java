package re.ermix.school_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.model.Enrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatusEnum status);

    List<Enrollment> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatusEnum status);

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    List<Enrollment> findByCourseIdAndStatus(Long courseId, EnrollmentStatusEnum status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    Long countActiveByCourseId(Long courseId);
}