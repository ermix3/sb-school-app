package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Optional<Course> getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }
    
    public List<Course> getCoursesByTitle(String title) {
        return courseRepository.findByTitle(title);
    }
    
    public List<Course> getCoursesByCredits(Integer credits) {
        return courseRepository.findByCredits(credits);
    }
    
    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }
    
    public List<Course> getCoursesWithAvailableSeats() {
        return courseRepository.findCoursesWithAvailableSeats();
    }
    
    public List<Course> getCoursesByTeacherSpecialty(String specialty) {
        return courseRepository.findByTeacherSpecialty(specialty);
    }
    
    public List<Course> getCoursesByStudent(Long studentId) {
        return courseRepository.findByStudentId(studentId);
    }
    
    public boolean isCourseAvailable(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            return false;
        }
        
        Course course = courseOpt.get();
        if (course.getMaxStudents() == null) {
            return true; // No limit on students
        }
        
        Long activeEnrollments = enrollmentRepository.countActiveByCourseId(courseId);
        return activeEnrollments < course.getMaxStudents();
    }
    
    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
    
    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    
    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setCourseCode(courseDetails.getCourseCode());
                    course.setTitle(courseDetails.getTitle());
                    course.setDescription(courseDetails.getDescription());
                    course.setCredits(courseDetails.getCredits());
                    course.setTeacher(courseDetails.getTeacher());
                    course.setMaxStudents(courseDetails.getMaxStudents());
                    return courseRepository.save(course);
                })
                .orElseGet(() -> {
                    courseDetails.setId(id);
                    return courseRepository.save(courseDetails);
                });
    }
}