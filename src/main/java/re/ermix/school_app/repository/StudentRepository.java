package re.ermix.school_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import re.ermix.school_app.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByEmail(String email);
    
    List<Student> findByLastName(String lastName);
    
    List<Student> findByFirstNameAndLastName(String firstName, String lastName);
    
    List<Student> findByEnrollmentDate(LocalDate enrollmentDate);
    
    @Query("SELECT s FROM Student s WHERE s.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Student> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.course.id = :courseId")
    List<Student> findByCourseId(Long courseId);
}