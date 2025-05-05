package re.ermix.school_app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
@Log4j2
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        log.info("GET /courses");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        log.info("GET /courses/{}", id);
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<Course> getCourseByCourseCode(@PathVariable String courseCode) {
        log.info("GET /courses/code/{}", courseCode);
        return courseService.getCourseByCourseCode(courseCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Course>> getCoursesByTitle(@PathVariable String title) {
        log.info("GET /courses/title/{}", title);
        List<Course> courses = courseService.getCoursesByTitle(title);
        return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
    }

    @GetMapping("/credits/{credits}")
    public ResponseEntity<List<Course>> getCoursesByCredits(@PathVariable Integer credits) {
        log.info("GET /courses/credits/{}", credits);
        List<Course> courses = courseService.getCoursesByCredits(credits);
        return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        log.info("GET /courses/teacher/{}", teacherId);
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Course>> getCoursesWithAvailableSeats() {
        log.info("GET /courses/available");
        List<Course> courses = courseService.getCoursesWithAvailableSeats();
        return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Course>> getCoursesByTeacherSpecialty(@PathVariable String specialty) {
        log.info("GET /courses/specialty/{}", specialty);
        List<Course> courses = courseService.getCoursesByTeacherSpecialty(specialty);
        return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Course>> getCoursesByStudent(@PathVariable Long studentId) {
        log.info("GET /courses/student/{}", studentId);
        List<Course> courses = courseService.getCoursesByStudent(studentId);
        return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> isCourseAvailable(@PathVariable Long id) {
        log.info("GET /courses/{}/available", id);
        return ResponseEntity.ok(courseService.isCourseAvailable(id));
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        log.info("POST /courses");
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        log.info("PUT /courses/{}", id);
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("DELETE /courses/{}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
