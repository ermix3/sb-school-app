package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.EnrollmentRepository;
import re.ermix.school_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    public List<Enrollment> getAllEnrollments() {
        log.info("Get all enrollments");
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(Long id) {
        log.info("Get enrollment by id: {}", id);
        return enrollmentRepository.findById(id);
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        log.info("Get enrollments by student id: {}", studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        log.info("Get enrollments by course id: {}", courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    public Optional<Enrollment> getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        log.info("Get enrollment by student id: {} and course id: {}", studentId, courseId);
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatusEnum status) {
        log.info("Get enrollments by status: {}", status);
        return enrollmentRepository.findByStatus(status);
    }

    public List<Enrollment> getEnrollmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Get enrollments by date range: {} to {}", startDate, endDate);
        return enrollmentRepository.findByEnrollmentDateBetween(startDate, endDate);
    }

    public List<Enrollment> getEnrollmentsByStudentAndStatus(Long studentId, EnrollmentStatusEnum status) {
        log.info("Get enrollments by student id: {} and status: {}", studentId, status);
        return enrollmentRepository.findByStudentIdAndStatus(studentId, status);
    }

    public List<Enrollment> getEnrollmentsByCourseAndStatus(Long courseId, EnrollmentStatusEnum status) {
        log.info("Get enrollments by course id: {} and status: {}", courseId, status);
        return enrollmentRepository.findByCourseIdAndStatus(courseId, status);
    }

    @Transactional
    public Enrollment enrollStudentInCourse(Long studentId, Long courseId, LocalDate enrollmentDate) {
        log.info("Enrolling student id: {} in course id: {} with date: {}", studentId, courseId, enrollmentDate);
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
            if (enrollment.getStatus() == EnrollmentStatusEnum.ACTIVE) {
                throw new IllegalStateException("Student is already enrolled in this course");
            } else if (enrollment.getStatus() == EnrollmentStatusEnum.DROPPED) {
                // Reactivate the enrollment
                enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);
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
        enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);

        // Add enrollment to student
        student.addEnrollment(enrollment);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatusEnum status) {
        log.info("Updating enrollment id: {} to status: {}", enrollmentId, status);
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> {
                    enrollment.setStatus(status);
                    return enrollmentRepository.save(enrollment);
                })
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found with id: " + enrollmentId));
    }

    @Transactional
    public void deleteEnrollment(Long id) {
        log.info("Deleting enrollment with id: {}", id);
        enrollmentRepository.deleteById(id);
    }
}
