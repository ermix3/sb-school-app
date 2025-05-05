package re.ermix.school_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Initialize a teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setEmail("jane.smith@example.com");

        // Initialize a course
        course = new Course();
        course.setId(1L);
        course.setCourseCode("MATH101");
        course.setTitle("Mathematics");
        course.setDescription("Introduction to Mathematics");
        course.setCredits(3);
        course.setTeacher(teacher);
        course.setMaxStudents(30);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCourseProperties() {
        assertEquals(1L, course.getId());
        assertEquals("MATH101", course.getCourseCode());
        assertEquals("Mathematics", course.getTitle());
        assertEquals("Introduction to Mathematics", course.getDescription());
        assertEquals(3, course.getCredits());
        assertEquals(teacher, course.getTeacher());
        assertEquals(30, course.getMaxStudents());
        assertNotNull(course.getCreatedAt());
        assertNotNull(course.getUpdatedAt());
    }

    @Test
    void testTeacherRelationship() {
        // Test the relationship with teacher
        assertNotNull(course.getTeacher());
        assertEquals("Jane", course.getTeacher().getFirstName());
        assertEquals("Smith", course.getTeacher().getLastName());
        assertEquals("jane.smith@example.com", course.getTeacher().getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create a copy of the course with the same ID but different properties
        Course sameIdCourse = new Course();
        sameIdCourse.setId(1L);

        // Create a different course with a different ID
        Course differentIdCourse = new Course();
        differentIdCourse.setId(2L);

        // Create a course with null ID
        Course nullIdCourse = new Course();

        // Test equals method
        assertEquals(course.getId(), sameIdCourse.getId(), "Courses with same ID should have equal IDs");
        assertNotEquals(course.getId(), differentIdCourse.getId(), "Courses with different IDs should have different IDs");

        // Test hashCode method
        assertEquals(course.getId().hashCode(), sameIdCourse.getId().hashCode(), 
                "Courses with same ID should have same hashCode for ID");
        assertNotEquals(course.getId().hashCode(), differentIdCourse.getId().hashCode(), 
                "Courses with different IDs should have different hashCode for ID");

        // Test null ID case
        assertNull(nullIdCourse.getId(), "New course should have null ID");
    }

    @Test
    void testToString() {
        // Test toString method (provided by Lombok @Data)
        String courseString = course.toString();

        // Verify the string contains important properties
        assertTrue(courseString.contains("id=1"));
        assertTrue(courseString.contains("courseCode=MATH101"));
        assertTrue(courseString.contains("title=Mathematics"));
        assertTrue(courseString.contains("credits=3"));
    }

    @Test
    void testAllArgsConstructor() {
        // Create a course using the all-args constructor
        Course newCourse = new Course(
            2L,
            "CS101",
            "Computer Science",
            "Introduction to Computer Science",
            4,
            teacher,
            40,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Verify properties were set correctly
        assertEquals(2L, newCourse.getId());
        assertEquals("CS101", newCourse.getCourseCode());
        assertEquals("Computer Science", newCourse.getTitle());
        assertEquals("Introduction to Computer Science", newCourse.getDescription());
        assertEquals(4, newCourse.getCredits());
        assertEquals(teacher, newCourse.getTeacher());
        assertEquals(40, newCourse.getMaxStudents());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a course using the no-args constructor
        Course newCourse = new Course();

        // Verify properties are null or default values
        assertNull(newCourse.getId());
        assertNull(newCourse.getCourseCode());
        assertNull(newCourse.getTitle());
        assertNull(newCourse.getDescription());
        assertNull(newCourse.getCredits());
        assertNull(newCourse.getTeacher());
        assertNull(newCourse.getMaxStudents());
        assertNull(newCourse.getCreatedAt());
        assertNull(newCourse.getUpdatedAt());
    }
}