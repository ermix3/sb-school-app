package re.ermix.school_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import re.ermix.school_app.model.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByEmail(String email);
    
    List<Teacher> findByLastName(String lastName);
    
    List<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
    
    List<Teacher> findBySubjectSpecialty(String subjectSpecialty);
    
    List<Teacher> findByHireDateAfter(LocalDate date);
    
    @Query("SELECT t FROM Teacher t JOIN Course c ON t.id = c.teacher.id WHERE c.id = :courseId")
    Optional<Teacher> findByCourseId(Long courseId);
}