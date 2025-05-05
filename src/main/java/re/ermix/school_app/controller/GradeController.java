package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.ermix.school_app.enums.GradeTypeEnum;
import re.ermix.school_app.model.Grade;
import re.ermix.school_app.service.GradeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("grades")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        log.info("GET /grades");
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        log.info("GET /grades/{}", id);
        return gradeService.getGradeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<Grade>> getGradesByEnrollment(@PathVariable Long enrollmentId) {
        log.info("GET /grades/enrollment/{}", enrollmentId);
        List<Grade> grades = gradeService.getGradesByEnrollment(enrollmentId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }

    @GetMapping("/type/{gradeTypeEnum}")
    public ResponseEntity<List<Grade>> getGradesByType(@PathVariable GradeTypeEnum gradeTypeEnum) {
        log.info("GET /grades/type/{}", gradeTypeEnum);
        List<Grade> grades = gradeService.getGradesByType(gradeTypeEnum);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }

    @GetMapping("/dateRange")
    public ResponseEntity<List<Grade>> getGradesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /grades/dateRange startDate={} endDate={}", startDate, endDate);
        List<Grade> grades = gradeService.getGradesByDateRange(startDate, endDate);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        log.info("GET /grades/student/{}", studentId);
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourse(@PathVariable Long courseId) {
        log.info("GET /grades/course/{}", courseId);
        List<Grade> grades = gradeService.getGradesByCourse(courseId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        log.info("GET /grades/student/{}/course/{}", studentId, courseId);
        List<Grade> grades = gradeService.getGradesByStudentAndCourse(studentId, courseId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }

    @GetMapping("/enrollment/{enrollmentId}/average")
    public ResponseEntity<BigDecimal> calculateAverageGradeForEnrollment(@PathVariable Long enrollmentId) {
        log.info("GET /grades/enrollment/{}/average", enrollmentId);
        BigDecimal average = gradeService.calculateAverageGradeForEnrollment(enrollmentId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<BigDecimal> calculateAverageGradeForStudent(@PathVariable Long studentId) {
        log.info("GET /grades/student/{}/average", studentId);
        BigDecimal average = gradeService.calculateAverageGradeForStudent(studentId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<BigDecimal> calculateAverageGradeForCourse(@PathVariable Long courseId) {
        log.info("GET /grades/course/{}/average", courseId);
        BigDecimal average = gradeService.calculateAverageGradeForCourse(courseId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Grade> addGradeToEnrollment(
            @RequestParam Long enrollmentId,
            @RequestParam BigDecimal gradeValue,
            @RequestParam GradeTypeEnum gradeTypeEnum,
            @RequestParam(required = false) String comment,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRecorded) {
        log.info("POST /grades enrollmentId={} gradeValue={} gradeTypeEnum={} dateRecorded={}",
                enrollmentId, gradeValue, gradeTypeEnum, dateRecorded);
        try {
            Grade grade = gradeService.addGradeToEnrollment(enrollmentId, gradeValue, gradeTypeEnum, comment, dateRecorded);
            return ResponseEntity.status(HttpStatus.CREATED).body(grade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
        log.info("PUT /grades/{}", id);
        try {
            Grade updatedGrade = gradeService.updateGrade(id, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        log.info("DELETE /grades/{}", id);
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
