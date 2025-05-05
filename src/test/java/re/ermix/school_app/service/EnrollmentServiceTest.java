package re.ermix.school_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.EnrollmentRepository;
import re.ermix.school_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student testStudent;
    private Course testCourse;
    private Enrollment testEnrollment;
    private LocalDate enrollmentDate;

    @BeforeEach
    void setUp() {
        // Set up test data
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setCourseCode("CS101");
        testCourse.setTitle("Introduction to Computer Science");

        enrollmentDate = LocalDate.of(2023, 9, 1);

        testEnrollment = new Enrollment();
        testEnrollment.setId(1L);
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
        testEnrollment.setEnrollmentDate(enrollmentDate);
        testEnrollment.setStatus(EnrollmentStatusEnum.ACTIVE);
    }

    @Test
    void getAllEnrollments_ShouldReturnAllEnrollments() {
        // Given
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findAll()).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void getEnrollmentById_WhenEnrollmentExists_ShouldReturnEnrollment() {
        // Given
        Long enrollmentId = 1L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(testEnrollment));

        // When
        Optional<Enrollment> result = enrollmentService.getEnrollmentById(enrollmentId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(enrollmentId);
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }

    @Test
    void getEnrollmentById_WhenEnrollmentDoesNotExist_ShouldReturnEmpty() {
        // Given
        Long enrollmentId = 999L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // When
        Optional<Enrollment> result = enrollmentService.getEnrollmentById(enrollmentId);

        // Then
        assertThat(result).isEmpty();
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }

    @Test
    void getEnrollmentsByStudent_ShouldReturnEnrollmentsByStudent() {
        // Given
        Long studentId = 1L;
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getEnrollmentsByStudent(studentId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    void getEnrollmentsByCourse_ShouldReturnEnrollmentsByCourse() {
        // Given
        Long courseId = 1L;
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findByCourseId(courseId)).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getEnrollmentsByCourse(courseId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findByCourseId(courseId);
    }

    @Test
    void getEnrollmentByStudentAndCourse_WhenEnrollmentExists_ShouldReturnEnrollment() {
        // Given
        Long studentId = 1L;
        Long courseId = 1L;
        when(enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.of(testEnrollment));

        // When
        Optional<Enrollment> result = enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStudent().getId()).isEqualTo(studentId);
        assertThat(result.get().getCourse().getId()).isEqualTo(courseId);
        verify(enrollmentRepository, times(1)).findByStudentIdAndCourseId(studentId, courseId);
    }

    @Test
    void getEnrollmentsByStatus_ShouldReturnEnrollmentsByStatus() {
        // Given
        EnrollmentStatusEnum status = EnrollmentStatusEnum.ACTIVE;
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findByStatus(status)).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getEnrollmentsByStatus(status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findByStatus(status);
    }

    @Test
    void getEnrollmentsByDateRange_ShouldReturnEnrollmentsInDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 1);
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findByEnrollmentDateBetween(startDate, endDate)).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getEnrollmentsByDateRange(startDate, endDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findByEnrollmentDateBetween(startDate, endDate);
    }

    @Test
    void getEnrollmentsByStudentAndStatus_ShouldReturnMatchingEnrollments() {
        // Given
        Long studentId = 1L;
        EnrollmentStatusEnum status = EnrollmentStatusEnum.ACTIVE;
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findByStudentIdAndStatus(studentId, status)).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getEnrollmentsByStudentAndStatus(studentId, status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findByStudentIdAndStatus(studentId, status);
    }

    @Test
    void getEnrollmentsByCourseAndStatus_ShouldReturnMatchingEnrollments() {
        // Given
        Long courseId = 1L;
        EnrollmentStatusEnum status = EnrollmentStatusEnum.ACTIVE;
        List<Enrollment> enrollments = Arrays.asList(testEnrollment);
        when(enrollmentRepository.findByCourseIdAndStatus(courseId, status)).thenReturn(enrollments);

        // When
        List<Enrollment> result = enrollmentService.getEnrollmentsByCourseAndStatus(courseId, status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(enrollments);
        verify(enrollmentRepository, times(1)).findByCourseIdAndStatus(courseId, status);
    }

    @Test
    void enrollStudentInCourse_WhenAllValid_ShouldCreateNewEnrollment() {
        // Given
        Long studentId = 1L;
        Long courseId = 1L;
        LocalDate enrollmentDate = LocalDate.now();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());
        when(courseService.isCourseAvailable(courseId)).thenReturn(true);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Enrollment result = enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStudent().getId()).isEqualTo(studentId);
        assertThat(result.getCourse().getId()).isEqualTo(courseId);
        assertThat(result.getEnrollmentDate()).isEqualTo(enrollmentDate);
        assertThat(result.getStatus()).isEqualTo(EnrollmentStatusEnum.ACTIVE);
        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(enrollmentRepository, times(1)).findByStudentIdAndCourseId(studentId, courseId);
        verify(courseService, times(1)).isCourseAvailable(courseId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enrollStudentInCourse_WhenStudentNotFound_ShouldThrowException() {
        // Given
        Long studentId = 999L;
        Long courseId = 1L;
        LocalDate enrollmentDate = LocalDate.now();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student not found");

        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, never()).findById(any());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudentInCourse_WhenCourseNotFound_ShouldThrowException() {
        // Given
        Long studentId = 1L;
        Long courseId = 999L;
        LocalDate enrollmentDate = LocalDate.now();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Course not found");

        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudentInCourse_WhenAlreadyEnrolled_ShouldThrowException() {
        // Given
        Long studentId = 1L;
        Long courseId = 1L;
        LocalDate enrollmentDate = LocalDate.now();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.of(testEnrollment)); // Active enrollment

        // When & Then
        assertThatThrownBy(() -> enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already enrolled");

        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(enrollmentRepository, times(1)).findByStudentIdAndCourseId(studentId, courseId);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enrollStudentInCourse_WhenPreviouslyDropped_ShouldReactivateEnrollment() {
        // Given
        Long studentId = 1L;
        Long courseId = 1L;
        LocalDate enrollmentDate = LocalDate.now();
        
        Enrollment droppedEnrollment = new Enrollment();
        droppedEnrollment.setId(1L);
        droppedEnrollment.setStudent(testStudent);
        droppedEnrollment.setCourse(testCourse);
        droppedEnrollment.setStatus(EnrollmentStatusEnum.DROPPED);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.of(droppedEnrollment)); 
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Enrollment result = enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(EnrollmentStatusEnum.ACTIVE);
        assertThat(result.getEnrollmentDate()).isEqualTo(enrollmentDate);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enrollStudentInCourse_WhenCourseIsFull_ShouldThrowException() {
        // Given
        Long studentId = 1L;
        Long courseId = 1L;
        LocalDate enrollmentDate = LocalDate.now();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());
        when(courseService.isCourseAvailable(courseId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> enrollmentService.enrollStudentInCourse(studentId, courseId, enrollmentDate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Course is full");

        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(enrollmentRepository, times(1)).findByStudentIdAndCourseId(studentId, courseId);
        verify(courseService, times(1)).isCourseAvailable(courseId);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void updateEnrollmentStatus_WhenEnrollmentExists_ShouldUpdateStatus() {
        // Given
        Long enrollmentId = 1L;
        EnrollmentStatusEnum newStatus = EnrollmentStatusEnum.COMPLETED;
        
        Enrollment updatedEnrollment = new Enrollment();
        updatedEnrollment.setId(enrollmentId);
        updatedEnrollment.setStatus(newStatus);

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(testEnrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(updatedEnrollment);

        // When
        Enrollment result = enrollmentService.updateEnrollmentStatus(enrollmentId, newStatus);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(enrollmentId);
        assertThat(result.getStatus()).isEqualTo(newStatus);
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void updateEnrollmentStatus_WhenEnrollmentDoesNotExist_ShouldThrowException() {
        // Given
        Long enrollmentId = 999L;
        EnrollmentStatusEnum newStatus = EnrollmentStatusEnum.DROPPED;

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.updateEnrollmentStatus(enrollmentId, newStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enrollment not found");

        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void deleteEnrollment_ShouldCallRepositoryDeleteMethod() {
        // Given
        Long enrollmentId = 1L;
        doNothing().when(enrollmentRepository).deleteById(enrollmentId);

        // When
        enrollmentService.deleteEnrollment(enrollmentId);

        // Then
        verify(enrollmentRepository, times(1)).deleteById(enrollmentId);
    }
}
