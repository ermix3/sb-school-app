package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.ermix.school_app.model.Grade;
import re.ermix.school_app.model.Grade.GradeType;
import re.ermix.school_app.service.GradeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("grades")
@RequiredArgsConstructor
public class GradeController {
    
    private final GradeService gradeService;
    
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        return gradeService.getGradeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<Grade>> getGradesByEnrollment(@PathVariable Long enrollmentId) {
        List<Grade> grades = gradeService.getGradesByEnrollment(enrollmentId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
    
    @GetMapping("/type/{gradeType}")
    public ResponseEntity<List<Grade>> getGradesByType(@PathVariable GradeType gradeType) {
        List<Grade> grades = gradeService.getGradesByType(gradeType);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
    
    @GetMapping("/dateRange")
    public ResponseEntity<List<Grade>> getGradesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Grade> grades = gradeService.getGradesByDateRange(startDate, endDate);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourse(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourse(courseId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
    
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByStudentAndCourse(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByStudentAndCourse(studentId, courseId);
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
    
    @GetMapping("/enrollment/{enrollmentId}/average")
    public ResponseEntity<BigDecimal> calculateAverageGradeForEnrollment(@PathVariable Long enrollmentId) {
        BigDecimal average = gradeService.calculateAverageGradeForEnrollment(enrollmentId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<BigDecimal> calculateAverageGradeForStudent(@PathVariable Long studentId) {
        BigDecimal average = gradeService.calculateAverageGradeForStudent(studentId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<BigDecimal> calculateAverageGradeForCourse(@PathVariable Long courseId) {
        BigDecimal average = gradeService.calculateAverageGradeForCourse(courseId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.noContent().build();
    }
    
    @PostMapping
    public ResponseEntity<Grade> addGradeToEnrollment(
            @RequestParam Long enrollmentId,
            @RequestParam BigDecimal gradeValue,
            @RequestParam GradeType gradeType,
            @RequestParam(required = false) String comment,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRecorded) {
        try {
            Grade grade = gradeService.addGradeToEnrollment(enrollmentId, gradeValue, gradeType, comment, dateRecorded);
            return ResponseEntity.status(HttpStatus.CREATED).body(grade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}