package re.ermix.school_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.enums.GradeTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    private Grade grade;
    private Enrollment enrollment;
    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        // Initialize a student
        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");

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

        // Initialize a grade
        grade = new Grade();
        grade.setId(1L);
        grade.setEnrollment(enrollment);
        grade.setGradeValue(new BigDecimal("85.50"));
        grade.setGradeType(GradeTypeEnum.MIDTERM);
        grade.setComment("Good performance");
        grade.setDateRecorded(LocalDate.of(2022, 10, 15));
        grade.setCreatedAt(LocalDateTime.now());
        grade.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGradeProperties() {
        assertEquals(1L, grade.getId());
        assertEquals(enrollment, grade.getEnrollment());
        assertEquals(new BigDecimal("85.50"), grade.getGradeValue());
        assertEquals(GradeTypeEnum.MIDTERM, grade.getGradeType());
        assertEquals("Good performance", grade.getComment());
        assertEquals(LocalDate.of(2022, 10, 15), grade.getDateRecorded());
        assertNotNull(grade.getCreatedAt());
        assertNotNull(grade.getUpdatedAt());
    }

    @Test
    void testEnrollmentRelationship() {
        // Test the relationship with enrollment
        assertNotNull(grade.getEnrollment());
        assertEquals(1L, grade.getEnrollment().getId());
        
        // Test the relationship with student through enrollment
        assertEquals("John", grade.getEnrollment().getStudent().getFirstName());
        assertEquals("Doe", grade.getEnrollment().getStudent().getLastName());
        
        // Test the relationship with course through enrollment
        assertEquals("Mathematics", grade.getEnrollment().getCourse().getTitle());
        assertEquals("MATH101", grade.getEnrollment().getCourse().getCourseCode());
    }

    @Test
    void testGradeTypeEnum() {
        // Test all enum values
        assertEquals(5, GradeTypeEnum.values().length);
        assertEquals(GradeTypeEnum.ASSIGNMENT, GradeTypeEnum.valueOf("ASSIGNMENT"));
        assertEquals(GradeTypeEnum.QUIZ, GradeTypeEnum.valueOf("QUIZ"));
        assertEquals(GradeTypeEnum.MIDTERM, GradeTypeEnum.valueOf("MIDTERM"));
        assertEquals(GradeTypeEnum.FINAL, GradeTypeEnum.valueOf("FINAL"));
        assertEquals(GradeTypeEnum.PROJECT, GradeTypeEnum.valueOf("PROJECT"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Create a copy of the grade with the same ID but different properties
        Grade sameIdGrade = new Grade();
        sameIdGrade.setId(1L);

        // Create a different grade with a different ID
        Grade differentIdGrade = new Grade();
        differentIdGrade.setId(2L);

        // Create a grade with null ID
        Grade nullIdGrade = new Grade();

        // Test equals method
        assertEquals(grade.getId(), sameIdGrade.getId(), "Grades with same ID should have equal IDs");
        assertNotEquals(grade.getId(), differentIdGrade.getId(), "Grades with different IDs should have different IDs");

        // Test hashCode method
        assertEquals(grade.getId().hashCode(), sameIdGrade.getId().hashCode(), 
                "Grades with same ID should have same hashCode for ID");
        assertNotEquals(grade.getId().hashCode(), differentIdGrade.getId().hashCode(), 
                "Grades with different IDs should have different hashCode for ID");

        // Test null ID case
        assertNull(nullIdGrade.getId(), "New grade should have null ID");
    }

    @Test
    void testToString() {
        // Test toString method (provided by Lombok @ToString)
        String gradeString = grade.toString();

        // Verify the string contains important properties
        assertTrue(gradeString.contains("id=1"));
        assertTrue(gradeString.contains("gradeValue=85.50"));
        assertTrue(gradeString.contains("gradeType=MIDTERM"));
        assertTrue(gradeString.contains("comment=Good performance"));
        
        // Verify enrollment is excluded from toString (as specified by @ToString(exclude = "enrollment"))
        assertFalse(gradeString.contains("enrollment="));
    }

    @Test
    void testAllArgsConstructor() {
        // Create a grade using the all-args constructor
        Grade newGrade = new Grade(
            2L,
            enrollment,
            new BigDecimal("92.75"),
            GradeTypeEnum.FINAL,
            "Excellent work",
            LocalDate.of(2022, 12, 20),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Verify properties were set correctly
        assertEquals(2L, newGrade.getId());
        assertEquals(enrollment, newGrade.getEnrollment());
        assertEquals(new BigDecimal("92.75"), newGrade.getGradeValue());
        assertEquals(GradeTypeEnum.FINAL, newGrade.getGradeType());
        assertEquals("Excellent work", newGrade.getComment());
        assertEquals(LocalDate.of(2022, 12, 20), newGrade.getDateRecorded());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a grade using the no-args constructor
        Grade newGrade = new Grade();

        // Verify properties are null or default values
        assertNull(newGrade.getId());
        assertNull(newGrade.getEnrollment());
        assertNull(newGrade.getGradeValue());
        assertNull(newGrade.getGradeType());
        assertNull(newGrade.getComment());
        assertNull(newGrade.getDateRecorded());
        assertNull(newGrade.getCreatedAt());
        assertNull(newGrade.getUpdatedAt());
    }
}