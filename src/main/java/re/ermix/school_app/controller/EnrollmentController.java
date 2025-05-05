package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Enrollment.EnrollmentStatus;
import re.ermix.school_app.service.EnrollmentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        return enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/dateRange")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByDateRange(startDate, endDate);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentAndStatus(
            @PathVariable Long studentId, @PathVariable EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentAndStatus(studentId, status);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course/{courseId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseAndStatus(
            @PathVariable Long courseId, @PathVariable EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseAndStatus(courseId, status);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }
    
    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollStudentInCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentDate) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateEnrollmentStatus(
            @PathVariable Long id, @RequestParam EnrollmentStatus status) {
        try {
            Enrollment enrollment = enrollmentService.updateEnrollmentStatus(id, status);
            return ResponseEntity.ok(enrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}