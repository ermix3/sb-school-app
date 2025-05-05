package re.ermix.school_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.enums.GradeTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    private Enrollment enrollment;
    private Student student;
    private Course course;
    private Grade grade;

    @BeforeEach
    void setUp() {
        // Initialize a student
        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        student.setEnrollments(new HashSet<>());

        // Initialize a course
        course = new Course();
        course.setId(1L);
        course.setTitle("Mathematics");
        course.setCourseCode("MATH101");
        course.setDescription("Introduction to Mathematics");
        course.setCredits(3);

        // Initialize an enrollment
        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);
        enrollment.setGrades(new HashSet<>());
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        // Initialize a grade
        grade = new Grade();
        grade.setId(1L);
        grade.setGradeValue(new BigDecimal("85.50"));
        grade.setGradeType(GradeTypeEnum.MIDTERM);
        grade.setComment("Good performance");
        grade.setDateRecorded(LocalDate.of(2022, 10, 15));
    }

    @Test
    void testEnrollmentProperties() {
        assertEquals(1L, enrollment.getId());
        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());
        assertEquals(LocalDate.of(2022, 9, 1), enrollment.getEnrollmentDate());
        assertEquals(EnrollmentStatusEnum.ACTIVE, enrollment.getStatus());
        assertNotNull(enrollment.getGrades());
        assertTrue(enrollment.getGrades().isEmpty());
        assertNotNull(enrollment.getCreatedAt());
        assertNotNull(enrollment.getUpdatedAt());
    }

    @Test
    void testStudentRelationship() {
        // Test the relationship with student
        assertNotNull(enrollment.getStudent());
        assertEquals("John", enrollment.getStudent().getFirstName());
        assertEquals("Doe", enrollment.getStudent().getLastName());
        assertEquals("john.doe@example.com", enrollment.getStudent().getEmail());
    }

    @Test
    void testCourseRelationship() {
        // Test the relationship with course
        assertNotNull(enrollment.getCourse());
        assertEquals("Mathematics", enrollment.getCourse().getTitle());
        assertEquals("MATH101", enrollment.getCourse().getCourseCode());
        assertEquals(3, enrollment.getCourse().getCredits());
    }

    @Test
    void testAddGrade() {
        // Test adding a grade
        enrollment.addGrade(grade);

        // Verify grade was added to enrollment
        assertEquals(1, enrollment.getGrades().size());
        assertTrue(enrollment.getGrades().contains(grade));

        // Verify enrollment reference was set in grade
        assertSame(enrollment, grade.getEnrollment(), "Enrollment reference should be set in grade");
    }

    @Test
    void testRemoveGrade() {
        // First add a grade
        enrollment.addGrade(grade);
        assertEquals(1, enrollment.getGrades().size());

        // Then test removing the grade
        enrollment.removeGrade(grade);

        // Verify grade was removed from enrollment
        assertEquals(0, enrollment.getGrades().size());
        assertFalse(enrollment.getGrades().contains(grade));

        // Verify enrollment reference was removed from grade
        assertNull(grade.getEnrollment(), "Enrollment reference should be null in grade");
    }

    @Test
    void testEnrollmentStatusEnum() {
        // Test all enum values
        assertEquals(3, EnrollmentStatusEnum.values().length);
        assertEquals(EnrollmentStatusEnum.ACTIVE, EnrollmentStatusEnum.valueOf("ACTIVE"));
        assertEquals(EnrollmentStatusEnum.DROPPED, EnrollmentStatusEnum.valueOf("DROPPED"));
        assertEquals(EnrollmentStatusEnum.COMPLETED, EnrollmentStatusEnum.valueOf("COMPLETED"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Create a copy of the enrollment with the same ID but different properties
        Enrollment sameIdEnrollment = new Enrollment();
        sameIdEnrollment.setId(1L);

        // Create a different enrollment with a different ID
        Enrollment differentIdEnrollment = new Enrollment();
        differentIdEnrollment.setId(2L);

        // Create an enrollment with null ID
        Enrollment nullIdEnrollment = new Enrollment();

        // Test equals method
        assertEquals(enrollment.getId(), sameIdEnrollment.getId(), "Enrollments with same ID should have equal IDs");
        assertNotEquals(enrollment.getId(), differentIdEnrollment.getId(), "Enrollments with different IDs should have different IDs");

        // Test hashCode method
        assertEquals(enrollment.getId().hashCode(), sameIdEnrollment.getId().hashCode(), 
                "Enrollments with same ID should have same hashCode for ID");
        assertNotEquals(enrollment.getId().hashCode(), differentIdEnrollment.getId().hashCode(), 
                "Enrollments with different IDs should have different hashCode for ID");

        // Test null ID case
        assertNull(nullIdEnrollment.getId(), "New enrollment should have null ID");
    }

    @Test
    void testToString() {
        // Test toString method (provided by Lombok @ToString)
        String enrollmentString = enrollment.toString();

        // Verify the string contains important properties
        assertTrue(enrollmentString.contains("id=1"));
        assertTrue(enrollmentString.contains("enrollmentDate=2022-09-01"));
        assertTrue(enrollmentString.contains("status=ACTIVE"));
        
        // Verify student and grades are excluded from toString (as specified by @ToString(exclude = {"student", "grades"}))
        assertFalse(enrollmentString.contains("student="));
        assertFalse(enrollmentString.contains("grades="));
    }

    @Test
    void testAllArgsConstructor() {
        // Create an enrollment using the all-args constructor
        Enrollment newEnrollment = new Enrollment(
            2L,
            student,
            course,
            LocalDate.of(2022, 8, 15),
            EnrollmentStatusEnum.COMPLETED,
            new HashSet<>(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Verify properties were set correctly
        assertEquals(2L, newEnrollment.getId());
        assertEquals(student, newEnrollment.getStudent());
        assertEquals(course, newEnrollment.getCourse());
        assertEquals(LocalDate.of(2022, 8, 15), newEnrollment.getEnrollmentDate());
        assertEquals(EnrollmentStatusEnum.COMPLETED, newEnrollment.getStatus());
        assertNotNull(newEnrollment.getGrades());
        assertTrue(newEnrollment.getGrades().isEmpty());
    }

    @Test
    void testNoArgsConstructor() {
        // Create an enrollment using the no-args constructor
        Enrollment newEnrollment = new Enrollment();

        // Verify properties are null or default values
        assertNull(newEnrollment.getId());
        assertNull(newEnrollment.getStudent());
        assertNull(newEnrollment.getCourse());
        assertNull(newEnrollment.getEnrollmentDate());
        assertEquals(EnrollmentStatusEnum.ACTIVE, newEnrollment.getStatus()); // Default value
        assertNotNull(newEnrollment.getGrades()); // Should be initialized to empty set
        assertTrue(newEnrollment.getGrades().isEmpty());
        assertNull(newEnrollment.getCreatedAt());
        assertNull(newEnrollment.getUpdatedAt());
    }
}