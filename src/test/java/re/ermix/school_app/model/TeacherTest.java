package re.ermix.school_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Initialize a teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setEmail("jane.smith@example.com");
        teacher.setPhoneNumber("555-987-6543");
        teacher.setHireDate(LocalDate.of(2020, 8, 15));
        teacher.setSubjectSpecialty("Mathematics");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testTeacherProperties() {
        assertEquals(1L, teacher.getId());
        assertEquals("Jane", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals("jane.smith@example.com", teacher.getEmail());
        assertEquals("555-987-6543", teacher.getPhoneNumber());
        assertEquals(LocalDate.of(2020, 8, 15), teacher.getHireDate());
        assertEquals("Mathematics", teacher.getSubjectSpecialty());
        assertNotNull(teacher.getCreatedAt());
        assertNotNull(teacher.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create a copy of the teacher with the same ID but different properties
        Teacher sameIdTeacher = new Teacher();
        sameIdTeacher.setId(1L);

        // Create a different teacher with a different ID
        Teacher differentIdTeacher = new Teacher();
        differentIdTeacher.setId(2L);

        // Create a teacher with null ID
        Teacher nullIdTeacher = new Teacher();

        // Test equals method
        assertEquals(teacher.getId(), sameIdTeacher.getId(), "Teachers with same ID should have equal IDs");
        assertNotEquals(teacher.getId(), differentIdTeacher.getId(), "Teachers with different IDs should have different IDs");

        // Test hashCode method
        assertEquals(teacher.getId().hashCode(), sameIdTeacher.getId().hashCode(), 
                "Teachers with same ID should have same hashCode for ID");
        assertNotEquals(teacher.getId().hashCode(), differentIdTeacher.getId().hashCode(), 
                "Teachers with different IDs should have different hashCode for ID");

        // Test null ID case
        assertNull(nullIdTeacher.getId(), "New teacher should have null ID");
    }

    @Test
    void testToString() {
        // Test toString method (provided by Lombok @Data)
        String teacherString = teacher.toString();

        // Verify the string contains important properties
        assertTrue(teacherString.contains("id=1"));
        assertTrue(teacherString.contains("firstName=Jane"));
        assertTrue(teacherString.contains("lastName=Smith"));
        assertTrue(teacherString.contains("email=jane.smith@example.com"));
        assertTrue(teacherString.contains("phoneNumber=555-987-6543"));
        assertTrue(teacherString.contains("subjectSpecialty=Mathematics"));
    }

    @Test
    void testAllArgsConstructor() {
        // Create a teacher using the all-args constructor
        Teacher newTeacher = new Teacher(
            2L,
            "John",
            "Doe",
            "john.doe@example.com",
            "555-123-4567",
            LocalDate.of(2019, 6, 10),
            "Computer Science",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Verify properties were set correctly
        assertEquals(2L, newTeacher.getId());
        assertEquals("John", newTeacher.getFirstName());
        assertEquals("Doe", newTeacher.getLastName());
        assertEquals("john.doe@example.com", newTeacher.getEmail());
        assertEquals("555-123-4567", newTeacher.getPhoneNumber());
        assertEquals(LocalDate.of(2019, 6, 10), newTeacher.getHireDate());
        assertEquals("Computer Science", newTeacher.getSubjectSpecialty());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a teacher using the no-args constructor
        Teacher newTeacher = new Teacher();

        // Verify properties are null or default values
        assertNull(newTeacher.getId());
        assertNull(newTeacher.getFirstName());
        assertNull(newTeacher.getLastName());
        assertNull(newTeacher.getEmail());
        assertNull(newTeacher.getPhoneNumber());
        assertNull(newTeacher.getHireDate());
        assertNull(newTeacher.getSubjectSpecialty());
        assertNull(newTeacher.getCreatedAt());
        assertNull(newTeacher.getUpdatedAt());
    }
}