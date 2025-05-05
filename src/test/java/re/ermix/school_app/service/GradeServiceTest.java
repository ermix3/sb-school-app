package re.ermix.school_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import re.ermix.school_app.enums.EnrollmentStatusEnum;
import re.ermix.school_app.enums.GradeTypeEnum;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Grade;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.repository.EnrollmentRepository;
import re.ermix.school_app.repository.GradeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private GradeService gradeService;

    private Grade testGrade;
    private Grade testGrade2;
    private Enrollment testEnrollment;
    private Student testStudent;
    private Course testCourse;
    private LocalDate dateRecorded;

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

        testEnrollment = new Enrollment();
        testEnrollment.setId(1L);
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
        testEnrollment.setStatus(EnrollmentStatusEnum.ACTIVE);

        dateRecorded = LocalDate.of(2023, 10, 15);

        testGrade = new Grade();
        testGrade.setId(1L);
        testGrade.setEnrollment(testEnrollment);
        testGrade.setGradeValue(new BigDecimal("85.00"));
        testGrade.setGradeType(GradeTypeEnum.MIDTERM);
        testGrade.setComment("Good work");
        testGrade.setDateRecorded(dateRecorded);

        testGrade2 = new Grade();
        testGrade2.setId(2L);
        testGrade2.setEnrollment(testEnrollment);
        testGrade2.setGradeValue(new BigDecimal("90.00"));
        testGrade2.setGradeType(GradeTypeEnum.FINAL);
        testGrade2.setDateRecorded(LocalDate.of(2023, 12, 15));
    }

    @Test
    void getAllGrades_ShouldReturnAllGrades() {
        // Given
        List<Grade> grades = Arrays.asList(testGrade, testGrade2);
        when(gradeRepository.findAll()).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getAllGrades();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(grades);
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    void getGradeById_WhenGradeExists_ShouldReturnGrade() {
        // Given
        Long gradeId = 1L;
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(testGrade));

        // When
        Optional<Grade> result = gradeService.getGradeById(gradeId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(gradeId);
        verify(gradeRepository, times(1)).findById(gradeId);
    }

    @Test
    void getGradeById_WhenGradeDoesNotExist_ShouldReturnEmpty() {
        // Given
        Long gradeId = 999L;
        when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

        // When
        Optional<Grade> result = gradeService.getGradeById(gradeId);

        // Then
        assertThat(result).isEmpty();
        verify(gradeRepository, times(1)).findById(gradeId);
    }

    @Test
    void getGradesByEnrollment_ShouldReturnGradesForEnrollment() {
        // Given
        Long enrollmentId = 1L;
        List<Grade> grades = Arrays.asList(testGrade, testGrade2);
        when(gradeRepository.findByEnrollmentId(enrollmentId)).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getGradesByEnrollment(enrollmentId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(grades);
        verify(gradeRepository, times(1)).findByEnrollmentId(enrollmentId);
    }

    @Test
    void getGradesByType_ShouldReturnGradesByType() {
        // Given
        GradeTypeEnum gradeType = GradeTypeEnum.MIDTERM;
        List<Grade> grades = Collections.singletonList(testGrade);
        when(gradeRepository.findByGradeType(gradeType)).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getGradesByType(gradeType);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGradeType()).isEqualTo(gradeType);
        verify(gradeRepository, times(1)).findByGradeType(gradeType);
    }

    @Test
    void getGradesByDateRange_ShouldReturnGradesInDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 11, 1);
        List<Grade> grades = Collections.singletonList(testGrade);
        when(gradeRepository.findByDateRecordedBetween(startDate, endDate)).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getGradesByDateRange(startDate, endDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(grades);
        verify(gradeRepository, times(1)).findByDateRecordedBetween(startDate, endDate);
    }

    @Test
    void getGradesByStudent_ShouldReturnGradesForStudent() {
        // Given
        Long studentId = 1L;
        List<Grade> grades = Arrays.asList(testGrade, testGrade2);
        when(gradeRepository.findByStudentId(studentId)).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getGradesByStudent(studentId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(grades);
        verify(gradeRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    void getGradesByCourse_ShouldReturnGradesForCourse() {
        // Given
        Long courseId = 1L;
        List<Grade> grades = Arrays.asList(testGrade, testGrade2);
        when(gradeRepository.findByCourseId(courseId)).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getGradesByCourse(courseId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(grades);
        verify(gradeRepository, times(1)).findByCourseId(courseId);
    }

    @Test
    void getGradesByStudentAndCourse_ShouldReturnGradesForStudentAndCourse() {
        // Given
        Long studentId = 1L;
        Long courseId = 1L;
        List<Grade> grades = Arrays.asList(testGrade, testGrade2);
        when(gradeRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(grades);

        // When
        List<Grade> result = gradeService.getGradesByStudentAndCourse(studentId, courseId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(grades);
        verify(gradeRepository, times(1)).findByStudentIdAndCourseId(studentId, courseId);
    }

    @Test
    void calculateAverageGradeForEnrollment_ShouldReturnAverageGrade() {
        // Given
        Long enrollmentId = 1L;
        BigDecimal averageGrade = new BigDecimal("87.50");
        when(gradeRepository.calculateAverageGradeForEnrollment(enrollmentId)).thenReturn(averageGrade);

        // When
        BigDecimal result = gradeService.calculateAverageGradeForEnrollment(enrollmentId);

        // Then
        assertThat(result).isEqualTo(averageGrade);
        verify(gradeRepository, times(1)).calculateAverageGradeForEnrollment(enrollmentId);
    }

    @Test
    void calculateAverageGradeForStudent_ShouldReturnAverageGrade() {
        // Given
        Long studentId = 1L;
        BigDecimal averageGrade = new BigDecimal("87.50");
        when(gradeRepository.calculateAverageGradeForStudent(studentId)).thenReturn(averageGrade);

        // When
        BigDecimal result = gradeService.calculateAverageGradeForStudent(studentId);

        // Then
        assertThat(result).isEqualTo(averageGrade);
        verify(gradeRepository, times(1)).calculateAverageGradeForStudent(studentId);
    }

    @Test
    void calculateAverageGradeForCourse_ShouldReturnAverageGrade() {
        // Given
        Long courseId = 1L;
        BigDecimal averageGrade = new BigDecimal("87.50");
        when(gradeRepository.calculateAverageGradeForCourse(courseId)).thenReturn(averageGrade);

        // When
        BigDecimal result = gradeService.calculateAverageGradeForCourse(courseId);

        // Then
        assertThat(result).isEqualTo(averageGrade);
        verify(gradeRepository, times(1)).calculateAverageGradeForCourse(courseId);
    }

    @Test
    void addGradeToEnrollment_WhenEnrollmentExists_ShouldAddGrade() {
        // Given
        Long enrollmentId = 1L;
        BigDecimal gradeValue = new BigDecimal("85.00");
        GradeTypeEnum gradeType = GradeTypeEnum.MIDTERM;
        String comment = "Good work";
        LocalDate dateRecorded = LocalDate.now();

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(testEnrollment));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> {
            Grade savedGrade = invocation.getArgument(0);
            savedGrade.setId(1L);
            return savedGrade;
        });

        // When
        Grade result = gradeService.addGradeToEnrollment(enrollmentId, gradeValue, gradeType, comment, dateRecorded);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEnrollment()).isEqualTo(testEnrollment);
        assertThat(result.getGradeValue()).isEqualTo(gradeValue);
        assertThat(result.getGradeType()).isEqualTo(gradeType);
        assertThat(result.getComment()).isEqualTo(comment);
        assertThat(result.getDateRecorded()).isEqualTo(dateRecorded);
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void addGradeToEnrollment_WhenEnrollmentDoesNotExist_ShouldThrowException() {
        // Given
        Long enrollmentId = 999L;
        BigDecimal gradeValue = new BigDecimal("85.00");
        GradeTypeEnum gradeType = GradeTypeEnum.MIDTERM;
        String comment = "Good work";
        LocalDate dateRecorded = LocalDate.now();

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> gradeService.addGradeToEnrollment(enrollmentId, gradeValue, gradeType, comment, dateRecorded))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enrollment not found");

        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void updateGrade_WhenGradeExists_ShouldUpdateGrade() {
        // Given
        Long gradeId = 1L;
        Grade updatedGradeDetails = new Grade();
        updatedGradeDetails.setGradeValue(new BigDecimal("90.00"));
        updatedGradeDetails.setGradeType(GradeTypeEnum.FINAL);
        updatedGradeDetails.setComment("Excellent work");
        updatedGradeDetails.setDateRecorded(LocalDate.of(2023, 12, 15));

        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(testGrade));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Grade result = gradeService.updateGrade(gradeId, updatedGradeDetails);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(gradeId);
        assertThat(result.getGradeValue()).isEqualTo(new BigDecimal("90.00"));
        assertThat(result.getGradeType()).isEqualTo(GradeTypeEnum.FINAL);
        assertThat(result.getComment()).isEqualTo("Excellent work");
        assertThat(result.getDateRecorded()).isEqualTo(LocalDate.of(2023, 12, 15));
        verify(gradeRepository, times(1)).findById(gradeId);
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void updateGrade_WhenGradeDoesNotExist_ShouldThrowException() {
        // Given
        Long gradeId = 999L;
        Grade updatedGradeDetails = new Grade();
        updatedGradeDetails.setGradeValue(new BigDecimal("90.00"));

        when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> gradeService.updateGrade(gradeId, updatedGradeDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Grade not found with id: " + gradeId);

        verify(gradeRepository, times(1)).findById(gradeId);
        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void deleteGrade_ShouldCallRepositoryDeleteMethod() {
        // Given
        Long gradeId = 1L;
        doNothing().when(gradeRepository).deleteById(gradeId);

        // When
        gradeService.deleteGrade(gradeId);

        // Then
        verify(gradeRepository, times(1)).deleteById(gradeId);
    }
}
