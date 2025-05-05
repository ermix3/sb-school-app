package re.ermix.school_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import re.ermix.school_app.enums.GradeTypeEnum;
import re.ermix.school_app.model.Grade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    List<Grade> findByEnrollmentId(Long enrollmentId);
    
    List<Grade> findByGradeType(GradeTypeEnum gradeType);
    
    List<Grade> findByDateRecordedBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT g FROM Grade g WHERE g.enrollment.student.id = :studentId")
    List<Grade> findByStudentId(Long studentId);
    
    @Query("SELECT g FROM Grade g WHERE g.enrollment.course.id = :courseId")
    List<Grade> findByCourseId(Long courseId);
    
    @Query("SELECT g FROM Grade g WHERE g.enrollment.student.id = :studentId AND g.enrollment.course.id = :courseId")
    List<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    @Query("SELECT AVG(g.gradeValue) FROM Grade g WHERE g.enrollment.id = :enrollmentId")
    BigDecimal calculateAverageGradeForEnrollment(Long enrollmentId);
    
    @Query("SELECT AVG(g.gradeValue) FROM Grade g WHERE g.enrollment.student.id = :studentId")
    BigDecimal calculateAverageGradeForStudent(Long studentId);
    
    @Query("SELECT AVG(g.gradeValue) FROM Grade g WHERE g.enrollment.course.id = :courseId")
    BigDecimal calculateAverageGradeForCourse(Long courseId);
}