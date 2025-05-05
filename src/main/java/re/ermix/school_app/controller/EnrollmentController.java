package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        log.info("GET /enrollments");
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        log.info("GET /enrollments/{}", id);
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        log.info("GET /enrollments/student/{}", studentId);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        log.info("GET /enrollments/course/{}", courseId);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        log.info("GET /enrollments/student/{}/course/{}", studentId, courseId);
        return enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        log.info("GET /enrollments/status/{}", status);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @GetMapping("/dateRange")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /enrollments/dateRange?startDate={}&endDate={}", startDate, endDate);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByDateRange(startDate, endDate);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentAndStatus(
            @PathVariable Long studentId, @PathVariable EnrollmentStatus status) {
        log.info("GET /enrollments/student/{}/status/{}", studentId, status);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentAndStatus(studentId, status);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseAndStatus(
            @PathVariable Long courseId, @PathVariable EnrollmentStatus status) {
        log.info("GET /enrollments/course/{}/status/{}", courseId, status);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseAndStatus(courseId, status);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollStudentInCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentDate) {
        log.info("POST /enrollments/enroll?studentId={}&courseId={}&enrollmentDate={}", studentId, courseId, enrollmentDate);
        try {
            Enrollment enrollment = enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate);
            log.info("Student {} successfully enrolled in course {}", studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to enroll student {} in course {}: {}", studentId, courseId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.warn("Failed to enroll student {} in course {}: {}", studentId, courseId, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateEnrollmentStatus(
            @PathVariable Long id, @RequestParam EnrollmentStatus status) {
        log.info("PUT /enrollments/{}/status?status={}", id, status);
        try {
            Enrollment enrollment = enrollmentService.updateEnrollmentStatus(id, status);
            log.info("Enrollment {} status updated to {}", id, status);
            return ResponseEntity.ok(enrollment);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to update enrollment {} status: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        log.info("DELETE /enrollments/{}", id);
        enrollmentService.deleteEnrollment(id);
        log.info("Enrollment {} deleted", id);
        return ResponseEntity.noContent().build();
    }
}
