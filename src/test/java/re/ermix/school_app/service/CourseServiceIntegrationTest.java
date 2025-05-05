package re.ermix.school_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.TestSchoolAppApplication;
import re.ermix.school_app.config.TestcontainersConfiguration;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Teacher;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the CourseService class.
 * These tests verify that CourseService correctly interacts with the database
 * and other components.
 */
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public class CourseServiceIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Create a test teacher
        testTeacher = new Teacher();
        testTeacher.setFirstName("Integration");
        testTeacher.setLastName("Test");
        testTeacher.setEmail("integration.test@example.com");
        testTeacher.setPhoneNumber("555-123-4567");
        testTeacher.setHireDate(java.time.LocalDate.now());
        testTeacher.setSubjectSpecialty("Computer Science");
        testTeacher = teacherRepository.save(testTeacher);

        // Create a test course
        testCourse = new Course();
        testCourse.setCourseCode("CS101-TEST");
        testCourse.setTitle("Introduction to Testing");
        testCourse.setDescription("Learn how to write effective tests");
        testCourse.setCredits(3);
        testCourse.setTeacher(testTeacher);
        testCourse = courseRepository.save(testCourse);
    }

    @Test
    void getAllCourses() {
        // Act
        List<Course> courses = courseService.getAllCourses();

        // Assert
        assertFalse(courses.isEmpty(), "Course list should not be empty");
        assertTrue(courses.stream().anyMatch(c -> c.getCourseCode().equals("CS101-TEST")),
                "Course list should contain the test course");
    }

    @Test
    void getCourseById() {
        // Act
        Optional<Course> foundCourse = courseService.getCourseById(testCourse.getId());

        // Assert
        assertTrue(foundCourse.isPresent(), "Course should be found by ID");
        assertEquals("CS101-TEST", foundCourse.get().getCourseCode(), "Course code should match");
        assertEquals("Introduction to Testing", foundCourse.get().getTitle(), "Course title should match");
    }

    @Test
    void getCoursesByCourseCode() {
        // Act
        Optional<Course> foundCourse = courseService.getCourseByCourseCode("CS101-TEST");

        // Assert
        assertTrue(foundCourse.isPresent(), "Course should be found by course code");
        assertEquals(testCourse.getId(), foundCourse.get().getId(), "Course ID should match");
    }

    @Test
    void getCoursesByTeacher() {
        // Act
        List<Course> courses = courseService.getCoursesByTeacher(testTeacher.getId());

        // Assert
        assertFalse(courses.isEmpty(), "Course list should not be empty");
        assertEquals(1, courses.size(), "Should find exactly one course");
        assertEquals("CS101-TEST", courses.get(0).getCourseCode(), "Course code should match");
    }

    @Test
    void saveCourse() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setCourseCode("CS102-TEST");
        newCourse.setTitle("Advanced Testing");
        newCourse.setDescription("Learn advanced testing techniques");
        newCourse.setCredits(4);
        newCourse.setTeacher(testTeacher);

        // Act
        Course savedCourse = courseService.saveCourse(newCourse);

        // Assert
        assertNotNull(savedCourse.getId(), "Saved course should have an ID");
        assertEquals("CS102-TEST", savedCourse.getCourseCode(), "Course code should match");
        assertEquals("Advanced Testing", savedCourse.getTitle(), "Course title should match");

        // Verify it's in the database
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertTrue(foundCourse.isPresent(), "Course should be found in the database");
    }

    @Test
    void updateCourse() {
        // Arrange
        testCourse.setTitle("Updated Test Course");
        testCourse.setDescription("This description has been updated");
        testCourse.setCredits(5);

        // Act
        Course updatedCourse = courseService.updateCourse(testCourse.getId(), testCourse);

        // Assert
        assertEquals("Updated Test Course", updatedCourse.getTitle(), "Course title should be updated");
        assertEquals("This description has been updated", updatedCourse.getDescription(), "Course description should be updated");
        assertEquals(5, updatedCourse.getCredits(), "Course credits should be updated");

        // Verify it's updated in the database
        Optional<Course> foundCourse = courseRepository.findById(testCourse.getId());
        assertTrue(foundCourse.isPresent(), "Course should be found in the database");
        assertEquals("Updated Test Course", foundCourse.get().getTitle(), "Course title should be updated in the database");
    }

    @Test
    void deleteCourse() {
        // Arrange
        Long courseId = testCourse.getId();

        // Act
        courseService.deleteCourse(courseId);

        // Assert
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        assertFalse(foundCourse.isPresent(), "Course should be deleted from the database");
    }

    @Test
    void testTransactionRollback() {
        // Arrange
        Course duplicateCourse = new Course();
        duplicateCourse.setCourseCode("CS101-TEST"); // Same code as existing course
        duplicateCourse.setTitle("Duplicate Course");
        duplicateCourse.setCredits(3);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            courseService.saveCourse(duplicateCourse);
        }, "Should throw exception for duplicate course code");

        // Verify the transaction was rolled back
        List<Course> courses = courseRepository.findAll();
        assertEquals(1, courses.stream().filter(c -> c.getCourseCode().equals("CS101-TEST")).count(),
                "There should still be only one course with the code CS101-TEST");
    }

    @Test
    void testCreatedAndUpdatedTimestamps() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setCourseCode("CS103-TEST");
        newCourse.setTitle("Timestamp Testing");
        newCourse.setCredits(3);
        newCourse.setTeacher(testTeacher);

        // Act
        Course savedCourse = courseService.saveCourse(newCourse);
        LocalDateTime createdAt = savedCourse.getCreatedAt();

        // Update the course
        savedCourse.setTitle("Updated Timestamp Testing");
        Course updatedCourse = courseService.updateCourse(savedCourse.getId(), savedCourse);

        // Assert
        assertNotNull(createdAt, "Created timestamp should not be null");
        assertNotNull(updatedCourse.getUpdatedAt(), "Updated timestamp should not be null");
        assertTrue(updatedCourse.getUpdatedAt().isAfter(createdAt) || 
                   updatedCourse.getUpdatedAt().equals(createdAt),
                   "Updated timestamp should be after or equal to created timestamp");
    }
}
