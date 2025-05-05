package re.ermix.school_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.model.StudentSearchCriteria;
import re.ermix.school_app.repository.StudentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private List<Student> studentList;

    @BeforeEach
    void setUp() {
        // Initialize test students
        student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@example.com");
        student1.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student1.setAddress("123 Main St");
        student1.setPhoneNumber("555-123-4567");
        student1.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        student1.setEnrollments(new HashSet<>());
        student1.setCreatedAt(LocalDateTime.now());
        student1.setUpdatedAt(LocalDateTime.now());

        student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@example.com");
        student2.setDateOfBirth(LocalDate.of(1999, 5, 15));
        student2.setAddress("456 Oak Ave");
        student2.setPhoneNumber("555-987-6543");
        student2.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        student2.setEnrollments(new HashSet<>());
        student2.setCreatedAt(LocalDateTime.now());
        student2.setUpdatedAt(LocalDateTime.now());

        studentList = Arrays.asList(student1, student2);
    }

    @Test
    void getAllStudents() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(studentList);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertEquals(2, result.size());
        assertEquals(student1.getId(), result.get(0).getId());
        assertEquals(student2.getId(), result.get(1).getId());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_whenStudentExists() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(student1.getId(), result.get().getId());
        assertEquals(student1.getFirstName(), result.get().getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getStudentById_whenStudentDoesNotExist() {
        // Arrange
        when(studentRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(3L);
    }

    @Test
    void getStudentByEmail_whenStudentExists() {
        // Arrange
        String targetEmail = "john.doe@example.com";
        var criteria = StudentSearchCriteria.builder().email(targetEmail).build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of(student1));

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(1, result.size());
        assertEquals(student1.getId(), result.get(0).getId());
        assertEquals(student1.getEmail(), result.get(0).getEmail());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getStudentByEmail_whenStudentDoesNotExist() {
        // Arrange
        String targetEmail = "nonexistent@example.com";
        var criteria = StudentSearchCriteria.builder().email(targetEmail).build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of());

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(0, result.size());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getStudentsByLastName() {
        // Arrange
        String lastName = "Smith";
        var criteria = StudentSearchCriteria.builder().lastName(lastName).build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of(student2));

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).getLastName());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getStudentsByName() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        var criteria = StudentSearchCriteria.builder().firstName(firstName).lastName(lastName).build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of(student1));

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getStudentsByEnrollmentDate() {
        // Arrange
        LocalDate enrollmentDate = LocalDate.of(2022, 9, 1);
        var criteria = StudentSearchCriteria.builder().enrollmentDate(enrollmentDate).build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(studentList);

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(2, result.size());
        assertEquals(enrollmentDate, result.get(0).getEnrollmentDate());
        assertEquals(enrollmentDate, result.get(1).getEnrollmentDate());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getStudentsByDateOfBirthRange() {
        // Arrange
        LocalDate startDate = LocalDate.of(1999, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 12, 31);
        var criteria = StudentSearchCriteria.builder()
                .dateOfBirthStart(startDate)
                .dateOfBirthEnd(endDate)
                .build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(studentList);

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getStudentsByCourse() {
        // Arrange
        Long courseId = 1L;
        var criteria = StudentSearchCriteria.builder().courseId(courseId).build();
        when(studentRepository.findAll(any(Specification.class))).thenReturn(List.of(student1));

        // Act
        List<Student> result = studentService.searchStudents(criteria);

        // Assert
        assertEquals(1, result.size());
        assertEquals(student1.getId(), result.get(0).getId());
        verify(studentRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void saveStudent() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setFirstName("New");
        newStudent.setLastName("Student");
        newStudent.setEmail("new.student@example.com");
        newStudent.setDateOfBirth(LocalDate.of(2001, 3, 15));
        newStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // Act
        Student result = studentService.saveStudent(newStudent);

        // Assert
        assertNotNull(result);
        assertEquals(newStudent.getFirstName(), result.getFirstName());
        assertEquals(newStudent.getLastName(), result.getLastName());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void deleteStudent() {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentRepository).deleteById(studentId);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void updateStudent_whenStudentExists() {
        // Arrange
        Long studentId = 1L;
        Student updatedDetails = new Student();
        updatedDetails.setFirstName("Updated");
        updatedDetails.setLastName("Name");
        updatedDetails.setEmail("updated.email@example.com");
        updatedDetails.setDateOfBirth(LocalDate.of(2000, 1, 1));
        updatedDetails.setAddress("Updated Address");
        updatedDetails.setPhoneNumber("555-999-8888");
        updatedDetails.setEnrollmentDate(LocalDate.of(2022, 9, 1));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Student result = studentService.updateStudent(studentId, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getId()); // ID should remain the same
        assertEquals(updatedDetails.getFirstName(), result.getFirstName());
        assertEquals(updatedDetails.getLastName(), result.getLastName());
        assertEquals(updatedDetails.getEmail(), result.getEmail());
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void updateStudent_whenStudentDoesNotExist() {
        // Arrange
        Long studentId = 3L;
        Student newStudent = new Student();
        newStudent.setFirstName("New");
        newStudent.setLastName("Student");
        newStudent.setEmail("new.student@example.com");
        newStudent.setDateOfBirth(LocalDate.of(2001, 3, 15));
        newStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.updateStudent(studentId, newStudent);
        });

        assertEquals("Student not found with id: " + studentId, exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void saveStudent_withDuplicateEmail() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setFirstName("Duplicate");
        newStudent.setLastName("Email");
        newStudent.setEmail("john.doe@example.com"); // Same email as student1
        newStudent.setDateOfBirth(LocalDate.of(2001, 3, 15));
        newStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        when(studentRepository.save(any(Student.class))).thenThrow(new DataIntegrityViolationException("Duplicate email"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.saveStudent(newStudent);
        });
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void updateStudent_withDuplicateEmail() {
        // Arrange
        Long studentId = 1L;
        Student updatedDetails = new Student();
        updatedDetails.setFirstName("Updated");
        updatedDetails.setLastName("Name");
        updatedDetails.setEmail("jane.smith@example.com"); // Same email as student2
        updatedDetails.setDateOfBirth(LocalDate.of(2000, 1, 1));
        updatedDetails.setAddress("Updated Address");
        updatedDetails.setPhoneNumber("555-999-8888");
        updatedDetails.setEnrollmentDate(LocalDate.of(2022, 9, 1));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenThrow(new DataIntegrityViolationException("Duplicate email"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.updateStudent(studentId, updatedDetails);
        });
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void saveStudent_withNullRequiredFields() {
        // Arrange
        Student invalidStudent = new Student();
        // Not setting required fields

        when(studentRepository.save(any(Student.class))).thenThrow(new DataIntegrityViolationException("Not-null property values"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.saveStudent(invalidStudent);
        });
        verify(studentRepository, times(1)).save(invalidStudent);
    }

    @Test
    void testAddEnrollment() {
        // Arrange
        Course course = new Course();
        course.setId(1L);
        course.setCourseCode("MATH101");
        course.setTitle("Mathematics");
        course.setCredits(3);

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);

        int initialSize = student1.getEnrollments().size();

        // Act
        student1.addEnrollment(enrollment);

        // Assert
        assertEquals(initialSize + 1, student1.getEnrollments().size());
        assertEquals(student1, enrollment.getStudent());

        // Verify the enrollment is in the set without using contains()
        boolean found = false;
        for (Enrollment e : student1.getEnrollments()) {
            if (e.getId().equals(enrollment.getId())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testRemoveEnrollment() {
        // Arrange
        Course course = new Course();
        course.setId(1L);
        course.setCourseCode("MATH101");
        course.setTitle("Mathematics");
        course.setCredits(3);

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);

        // Clear existing enrollments and add our test enrollment using the method we've verified works
        student1.getEnrollments().clear();
        student1.addEnrollment(enrollment);

        // Verify setup is correct
        assertEquals(1, student1.getEnrollments().size());
        assertEquals(student1, enrollment.getStudent());

        // Act
        student1.removeEnrollment(enrollment);

        // Assert
        assertEquals(0, student1.getEnrollments().size());
        assertNull(enrollment.getStudent());
    }

    @Test
    void testStudentWithMultipleEnrollments() {
        // Arrange
        Course course1 = new Course();
        course1.setId(1L);
        course1.setCourseCode("MATH101");
        course1.setTitle("Mathematics");
        course1.setCredits(3);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setCourseCode("PHYS101");
        course2.setTitle("Physics");
        course2.setCredits(4);

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId(1L);
        enrollment1.setCourse(course1);
        enrollment1.setEnrollmentDate(LocalDate.now());
        enrollment1.setStatus(EnrollmentStatusEnum.ACTIVE);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(2L);
        enrollment2.setCourse(course2);
        enrollment2.setEnrollmentDate(LocalDate.now());
        enrollment2.setStatus(EnrollmentStatusEnum.ACTIVE);

        student1.getEnrollments().clear(); // Clear any existing enrollments
        int initialSize = student1.getEnrollments().size();

        // Act
        student1.addEnrollment(enrollment1);
        student1.addEnrollment(enrollment2);

        // Assert
        assertEquals(initialSize + 2, student1.getEnrollments().size());
        assertEquals(student1, enrollment1.getStudent());
        assertEquals(student1, enrollment2.getStudent());

        // Verify both enrollments are in the set without using contains()
        boolean found1 = false;
        boolean found2 = false;
        for (Enrollment e : student1.getEnrollments()) {
            if (e.getId().equals(enrollment1.getId())) {
                found1 = true;
            }
            if (e.getId().equals(enrollment2.getId())) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }
}
