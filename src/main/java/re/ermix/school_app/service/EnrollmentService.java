package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Enrollment.EnrollmentStatus;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.EnrollmentRepository;
import re.ermix.school_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
    
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }
    
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
    
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
    
    public Optional<Enrollment> getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }
    
    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }
    
    public List<Enrollment> getEnrollmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return enrollmentRepository.findByEnrollmentDateBetween(startDate, endDate);
    }
    
    public List<Enrollment> getEnrollmentsByStudentAndStatus(Long studentId, EnrollmentStatus status) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, status);
    }
    
    public List<Enrollment> getEnrollmentsByCourseAndStatus(Long courseId, EnrollmentStatus status) {
        return enrollmentRepository.findByCourseIdAndStatus(courseId, status);
    }
    
    @Transactional
    public Enrollment enrollStudentInCourse(Long studentId, Long courseId, LocalDate enrollmentDate) {
        // Check if student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
        
        // Check if course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        
        // Check if student is already enrolled in this course
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existingEnrollment.isPresent()) {
            Enrollment enrollment = existingEnrollment.get();
            if (enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
                throw new IllegalStateException("Student is already enrolled in this course");
            } else if (enrollment.getStatus() == EnrollmentStatus.DROPPED) {
                // Reactivate the enrollment
                enrollment.setStatus(EnrollmentStatus.ACTIVE);
                enrollment.setEnrollmentDate(enrollmentDate);
                return enrollmentRepository.save(enrollment);
            }
        }
        
        // Check if course has available seats
        if (!courseService.isCourseAvailable(courseId)) {
            throw new IllegalStateException("Course is full and cannot accept more students");
        }
        
        // Create new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(enrollmentDate);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        
        // Add enrollment to student
        student.addEnrollment(enrollment);
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Transactional
    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status) {
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> {
                    enrollment.setStatus(status);
                    return enrollmentRepository.save(enrollment);
                })
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with id: " + enrollmentId));
    }
    
    @Transactional
    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }
}