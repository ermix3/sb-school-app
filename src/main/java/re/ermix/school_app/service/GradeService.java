package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Grade;
import re.ermix.school_app.model.Grade.GradeType;
import re.ermix.school_app.repository.EnrollmentRepository;
import re.ermix.school_app.repository.GradeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {
    
    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
    
    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }
    
    public List<Grade> getGradesByEnrollment(Long enrollmentId) {
        return gradeRepository.findByEnrollmentId(enrollmentId);
    }
    
    public List<Grade> getGradesByType(GradeType gradeType) {
        return gradeRepository.findByGradeType(gradeType);
    }
    
    public List<Grade> getGradesByDateRange(LocalDate startDate, LocalDate endDate) {
        return gradeRepository.findByDateRecordedBetween(startDate, endDate);
    }
    
    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    
    public List<Grade> getGradesByCourse(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }
    
    public List<Grade> getGradesByStudentAndCourse(Long studentId, Long courseId) {
        return gradeRepository.findByStudentIdAndCourseId(studentId, courseId);
    }
    
    public BigDecimal calculateAverageGradeForEnrollment(Long enrollmentId) {
        return gradeRepository.calculateAverageGradeForEnrollment(enrollmentId);
    }
    
    public BigDecimal calculateAverageGradeForStudent(Long studentId) {
        return gradeRepository.calculateAverageGradeForStudent(studentId);
    }
    
    public BigDecimal calculateAverageGradeForCourse(Long courseId) {
        return gradeRepository.calculateAverageGradeForCourse(courseId);
    }
    
    @Transactional
    public Grade addGradeToEnrollment(Long enrollmentId, BigDecimal gradeValue, GradeType gradeType, 
                                     String comment, LocalDate dateRecorded) {
        // Check if enrollment exists
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with id: " + enrollmentId));
        
        // Create new grade
        Grade grade = new Grade();
        grade.setEnrollment(enrollment);
        grade.setGradeValue(gradeValue);
        grade.setGradeType(gradeType);
        grade.setComment(comment);
        grade.setDateRecorded(dateRecorded);
        
        // Add grade to enrollment
        enrollment.addGrade(grade);
        
        return gradeRepository.save(grade);
    }
    
    @Transactional
    public Grade updateGrade(Long id, Grade gradeDetails) {
        return gradeRepository.findById(id)
                .map(grade -> {
                    grade.setGradeValue(gradeDetails.getGradeValue());
                    grade.setGradeType(gradeDetails.getGradeType());
                    grade.setComment(gradeDetails.getComment());
                    grade.setDateRecorded(gradeDetails.getDateRecorded());
                    return gradeRepository.save(grade);
                })
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id: " + id));
    }
    
    @Transactional
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}