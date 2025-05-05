package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.service.StudentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("students")
@RequiredArgsConstructor
@Log4j2
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        log.info("GET /students");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        log.info("GET /students/{}", id);
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        log.info("GET /students/email/{}", email);
        return studentService.getStudentByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<Student>> getStudentsByLastName(@PathVariable String lastName) {
        log.info("GET /students/lastName/{}", lastName);
        List<Student> students = studentService.getStudentsByLastName(lastName);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/name")
    public ResponseEntity<List<Student>> getStudentsByName(
            @RequestParam String firstName, @RequestParam String lastName) {
        log.info("GET /students/name firstName={} lastName={}", firstName, lastName);
        List<Student> students = studentService.getStudentsByName(firstName, lastName);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/enrollmentDate/{date}")
    public ResponseEntity<List<Student>> getStudentsByEnrollmentDate(@PathVariable LocalDate date) {
        log.info("GET /students/enrollmentDate/{}", date);
        List<Student> students = studentService.getStudentsByEnrollmentDate(date);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/dateOfBirth")
    public ResponseEntity<List<Student>> getStudentsByDateOfBirthRange(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        log.info("GET /students/dateOfBirth startDate={} endDate={}", startDate, endDate);
        List<Student> students = studentService.getStudentsByDateOfBirthRange(startDate, endDate);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Student>> getStudentsByCourse(@PathVariable Long courseId) {
        log.info("GET /students/course/{}", courseId);
        List<Student> students = studentService.getStudentsByCourse(courseId);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        log.info("POST /students");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
