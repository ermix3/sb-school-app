package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.ermix.school_app.model.Teacher;
import re.ermix.school_app.service.TeacherService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("teachers")
@RequiredArgsConstructor
@Log4j2
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        log.info("GET /teachers");
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        log.info("GET /teachers/{}", id);
        return teacherService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Teacher> getTeacherByEmail(@PathVariable String email) {
        log.info("GET /teachers/email/{}", email);
        return teacherService.getTeacherByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<Teacher>> getTeachersByLastName(@PathVariable String lastName) {
        log.info("GET /teachers/lastName/{}", lastName);
        List<Teacher> teachers = teacherService.getTeachersByLastName(lastName);
        return teachers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teachers);
    }

    @GetMapping("/name")
    public ResponseEntity<List<Teacher>> getTeachersByName(
            @RequestParam String firstName, @RequestParam String lastName) {
        log.info("GET /teachers/name firstName={} lastName={}", firstName, lastName);
        List<Teacher> teachers = teacherService.getTeachersByName(firstName, lastName);
        return teachers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teachers);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Teacher>> getTeachersBySubjectSpecialty(@PathVariable String specialty) {
        log.info("GET /teachers/specialty/{}", specialty);
        List<Teacher> teachers = teacherService.getTeachersBySubjectSpecialty(specialty);
        return teachers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teachers);
    }

    @GetMapping("/hiredAfter/{date}")
    public ResponseEntity<List<Teacher>> getTeachersHiredAfter(@PathVariable LocalDate date) {
        log.info("GET /teachers/hiredAfter/{}", date);
        List<Teacher> teachers = teacherService.getTeachersHiredAfter(date);
        return teachers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teachers);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Teacher> getTeacherByCourse(@PathVariable Long courseId) {
        log.info("GET /teachers/course/{}", courseId);
        return teacherService.getTeacherByCourse(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        log.info("POST /teachers");
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.saveTeacher(teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        log.info("PUT /teachers/{}", id);
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        log.info("DELETE /teachers/{}", id);
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
