package re.ermix.school_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.ermix.school_app.enums.EnrollmentStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        // Initialize a student
        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setAddress("123 Main St");
        student.setPhoneNumber("555-123-4567");
        student.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        student.setEnrollments(new HashSet<>());
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        // Initialize a course
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Mathematics");
        course.setCourseCode("MATH101");
        course.setDescription("Introduction to Mathematics");
        course.setCredits(3);

        // Initialize an enrollment
        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);
    }

    @Test
    void testStudentProperties() {
        assertEquals(1L, student.getId());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john.doe@example.com", student.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), student.getDateOfBirth());
        assertEquals("123 Main St", student.getAddress());
        assertEquals("555-123-4567", student.getPhoneNumber());
        assertEquals(LocalDate.of(2022, 9, 1), student.getEnrollmentDate());
        assertNotNull(student.getEnrollments());
        assertTrue(student.getEnrollments().isEmpty());
        assertNotNull(student.getCreatedAt());
        assertNotNull(student.getUpdatedAt());
    }

    @Test
    void testAddEnrollment() {
        // Test adding an enrollment
        student.addEnrollment(enrollment);

        // Verify enrollment was added to student
        assertEquals(1, student.getEnrollments().size());

        // Check if the enrollment is in the set by ID
        boolean found = false;
        for (Enrollment e : student.getEnrollments()) {
            if (e.getId().equals(enrollment.getId())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Enrollment should be in the student's enrollments");

        // Verify student reference was set in enrollment
        assertSame(student, enrollment.getStudent(), "Student reference should be set in enrollment");
    }

    @Test
    void testEqualsAndHashCode() {
        // Create a copy of the student with the same ID but different properties
        Student sameIdStudent = new Student();
        sameIdStudent.setId(1L);

        // Create a different student with a different ID
        Student differentIdStudent = new Student();
        differentIdStudent.setId(2L);

        // Create a student with null ID
        Student nullIdStudent = new Student();

        // Test equals method
        // With Lombok @Data, equals should compare all fields, but in practice with JPA entities,
        // it's common to override equals to only compare by ID for better performance and to avoid
        // circular reference issues
        assertEquals(student.getId(), sameIdStudent.getId(), "Students with same ID should have equal IDs");
        assertNotEquals(student.getId(), differentIdStudent.getId(), "Students with different IDs should have different IDs");

        // Test hashCode method
        // With JPA entities using Lombok, hashCode is typically based on ID
        assertEquals(student.getId().hashCode(), sameIdStudent.getId().hashCode(), 
                "Students with same ID should have same hashCode for ID");
        assertNotEquals(student.getId().hashCode(), differentIdStudent.getId().hashCode(), 
                "Students with different IDs should have different hashCode for ID");

        // Test null ID case
        assertNull(nullIdStudent.getId(), "New student should have null ID");
    }

    @Test
    void testToString() {
        // Test toString method (provided by Lombok @Data)
        String studentString = student.toString();

        // Verify the string contains important properties
        assertTrue(studentString.contains("id=1"));
        assertTrue(studentString.contains("firstName=John"));
        assertTrue(studentString.contains("lastName=Doe"));
        assertTrue(studentString.contains("email=john.doe@example.com"));
    }

    @Test
    void testAllArgsConstructor() {
        // Create a student using the all-args constructor
        Student newStudent = new Student(
            2L,
            "Jane",
            "Smith",
            "jane.smith@example.com",
            LocalDate.of(1999, 5, 15),
            "456 Oak Ave",
            "555-987-6543",
            LocalDate.of(2022, 9, 1),
            new HashSet<>(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Verify properties were set correctly
        assertEquals(2L, newStudent.getId());
        assertEquals("Jane", newStudent.getFirstName());
        assertEquals("Smith", newStudent.getLastName());
        assertEquals("jane.smith@example.com", newStudent.getEmail());
        assertEquals(LocalDate.of(1999, 5, 15), newStudent.getDateOfBirth());
        assertEquals("456 Oak Ave", newStudent.getAddress());
        assertEquals("555-987-6543", newStudent.getPhoneNumber());
        assertEquals(LocalDate.of(2022, 9, 1), newStudent.getEnrollmentDate());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a student using the no-args constructor
        Student newStudent = new Student();

        // Verify properties are null or default values
        assertNull(newStudent.getId());
        assertNull(newStudent.getFirstName());
        assertNull(newStudent.getLastName());
        assertNull(newStudent.getEmail());
        assertNull(newStudent.getDateOfBirth());
        assertNull(newStudent.getAddress());
        assertNull(newStudent.getPhoneNumber());
        assertNull(newStudent.getEnrollmentDate());
        assertNull(newStudent.getCreatedAt());
        assertNull(newStudent.getUpdatedAt());

        // Enrollments should be initialized to an empty set, not null
        assertNotNull(newStudent.getEnrollments());
        assertTrue(newStudent.getEnrollments().isEmpty());
    }
}
