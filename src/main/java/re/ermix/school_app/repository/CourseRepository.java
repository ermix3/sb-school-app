package re.ermix.school_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import re.ermix.school_app.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCourseCode(String courseCode);
    
    List<Course> findByTitle(String title);
    
    List<Course> findByCredits(Integer credits);
    
    List<Course> findByTeacherId(Long teacherId);
    
    @Query("SELECT c FROM Course c WHERE c.maxStudents > (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = c.id AND e.status = 'ACTIVE')")
    List<Course> findCoursesWithAvailableSeats();
    
    @Query("SELECT c FROM Course c JOIN c.teacher t WHERE t.subjectSpecialty = :specialty")
    List<Course> findByTeacherSpecialty(String specialty);
    
    @Query("SELECT c FROM Course c JOIN Enrollment e ON c.id = e.course.id WHERE e.student.id = :studentId")
    List<Course> findByStudentId(Long studentId);
}