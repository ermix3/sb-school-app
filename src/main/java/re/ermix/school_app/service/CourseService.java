package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<Course> getAllCourses() {
        log.info("Get all courses");
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        log.info("Get course by id: {}", id);
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCourseCode(String courseCode) {
        log.info("Get course by course code: {}", courseCode);
        return courseRepository.findByCourseCode(courseCode);
    }

    public List<Course> getCoursesByTitle(String title) {
        log.info("Get courses by title: {}", title);
        return courseRepository.findByTitle(title);
    }

    public List<Course> getCoursesByCredits(Integer credits) {
        log.info("Get courses by credits: {}", credits);
        return courseRepository.findByCredits(credits);
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        log.info("Get courses by teacher id: {}", teacherId);
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Course> getCoursesWithAvailableSeats() {
        log.info("Get courses with available seats");
        return courseRepository.findCoursesWithAvailableSeats();
    }

    public List<Course> getCoursesByTeacherSpecialty(String specialty) {
        log.info("Get courses by teacher specialty: {}", specialty);
        return courseRepository.findByTeacherSpecialty(specialty);
    }

    public List<Course> getCoursesByStudent(Long studentId) {
        log.info("Get courses by student id: {}", studentId);
        return courseRepository.findByStudentId(studentId);
    }

    public boolean isCourseAvailable(Long courseId) {
        log.info("Check if course is available: {}", courseId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            log.info("Course not found: {}", courseId);
            return false;
        }

        Course course = courseOpt.get();
        if (course.getMaxStudents() == null) {
            log.info("Course has no student limit: {}", courseId);
            return true; // No limit on students
        }

        Long activeEnrollments = enrollmentRepository.countActiveByCourseId(courseId);
        boolean isAvailable = activeEnrollments < course.getMaxStudents();
        log.info("Course {} availability: {} (active enrollments: {}, max students: {})", 
                courseId, isAvailable, activeEnrollments, course.getMaxStudents());
        return isAvailable;
    }

    @Transactional
    public Course saveCourse(Course course) {
        log.info("Save course: {}", course.getCourseCode());
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        log.info("Delete course with id: {}", id);
        courseRepository.deleteById(id);
    }

    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        log.info("Update course with id: {}", id);
        return courseRepository.findById(id)
                .map(course -> {
                    course.setCourseCode(courseDetails.getCourseCode());
                    course.setTitle(courseDetails.getTitle());
                    course.setDescription(courseDetails.getDescription());
                    course.setCredits(courseDetails.getCredits());
                    course.setTeacher(courseDetails.getTeacher());
                    course.setMaxStudents(courseDetails.getMaxStudents());
                    log.info("Updating existing course: {}", course.getCourseCode());
                    return courseRepository.save(course);
                })
                .orElseGet(() -> {
                    courseDetails.setId(id);
                    log.info("Creating new course with id: {}", id);
                    return courseRepository.save(courseDetails);
                });
    }
}
