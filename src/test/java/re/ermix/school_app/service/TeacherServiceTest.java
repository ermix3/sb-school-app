package re.ermix.school_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import re.ermix.school_app.model.Teacher;
import re.ermix.school_app.repository.TeacherRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;
    private Teacher testTeacher2;
    private LocalDate hireDate;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Set up test data
        now = LocalDateTime.now();
        hireDate = LocalDate.of(2020, 8, 15);

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setFirstName("Jane");
        testTeacher.setLastName("Smith");
        testTeacher.setEmail("jane.smith@example.com");
        testTeacher.setPhoneNumber("555-987-6543");
        testTeacher.setHireDate(hireDate);
        testTeacher.setSubjectSpecialty("Mathematics");
        testTeacher.setCreatedAt(now);
        testTeacher.setUpdatedAt(now);

        testTeacher2 = new Teacher();
        testTeacher2.setId(2L);
        testTeacher2.setFirstName("John");
        testTeacher2.setLastName("Doe");
        testTeacher2.setEmail("john.doe@example.com");
        testTeacher2.setPhoneNumber("555-123-4567");
        testTeacher2.setHireDate(LocalDate.of(2019, 6, 10));
        testTeacher2.setSubjectSpecialty("Computer Science");
        testTeacher2.setCreatedAt(now);
        testTeacher2.setUpdatedAt(now);
    }

    @Test
    void getAllTeachers_ShouldReturnAllTeachers() {
        // Given
        List<Teacher> teachers = Arrays.asList(testTeacher, testTeacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // When
        List<Teacher> result = teacherService.getAllTeachers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(teachers);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void getTeacherById_WhenTeacherExists_ShouldReturnTeacher() {
        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(testTeacher));

        // When
        Optional<Teacher> result = teacherService.getTeacherById(teacherId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(teacherId);
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void getTeacherById_WhenTeacherDoesNotExist_ShouldReturnEmpty() {
        // Given
        Long teacherId = 999L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When
        Optional<Teacher> result = teacherService.getTeacherById(teacherId);

        // Then
        assertThat(result).isEmpty();
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void getTeacherByEmail_WhenTeacherExists_ShouldReturnTeacher() {
        // Given
        String email = "jane.smith@example.com";
        when(teacherRepository.findByEmail(email)).thenReturn(Optional.of(testTeacher));

        // When
        Optional<Teacher> result = teacherService.getTeacherByEmail(email);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        verify(teacherRepository, times(1)).findByEmail(email);
    }

    @Test
    void getTeacherByEmail_WhenTeacherDoesNotExist_ShouldReturnEmpty() {
        // Given
        String email = "nonexistent@example.com";
        when(teacherRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<Teacher> result = teacherService.getTeacherByEmail(email);

        // Then
        assertThat(result).isEmpty();
        verify(teacherRepository, times(1)).findByEmail(email);
    }

    @Test
    void getTeachersByLastName_ShouldReturnMatchingTeachers() {
        // Given
        String lastName = "Smith";
        when(teacherRepository.findByLastName(lastName)).thenReturn(Collections.singletonList(testTeacher));

        // When
        List<Teacher> result = teacherService.getTeachersByLastName(lastName);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo(lastName);
        verify(teacherRepository, times(1)).findByLastName(lastName);
    }

    @Test
    void getTeachersByName_ShouldReturnMatchingTeachers() {
        // Given
        String firstName = "Jane";
        String lastName = "Smith";
        when(teacherRepository.findByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Collections.singletonList(testTeacher));

        // When
        List<Teacher> result = teacherService.getTeachersByName(firstName, lastName);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(result.get(0).getLastName()).isEqualTo(lastName);
        verify(teacherRepository, times(1)).findByFirstNameAndLastName(firstName, lastName);
    }

    @Test
    void getTeachersBySubjectSpecialty_ShouldReturnMatchingTeachers() {
        // Given
        String specialty = "Mathematics";
        when(teacherRepository.findBySubjectSpecialty(specialty))
                .thenReturn(Collections.singletonList(testTeacher));

        // When
        List<Teacher> result = teacherService.getTeachersBySubjectSpecialty(specialty);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubjectSpecialty()).isEqualTo(specialty);
        verify(teacherRepository, times(1)).findBySubjectSpecialty(specialty);
    }

    @Test
    void getTeachersHiredAfter_ShouldReturnMatchingTeachers() {
        // Given
        LocalDate date = LocalDate.of(2020, 1, 1);
        List<Teacher> teachers = Collections.singletonList(testTeacher);
        when(teacherRepository.findByHireDateAfter(date)).thenReturn(teachers);

        // When
        List<Teacher> result = teacherService.getTeachersHiredAfter(date);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHireDate()).isAfter(date);
        verify(teacherRepository, times(1)).findByHireDateAfter(date);
    }

    @Test
    void getTeacherByCourse_WhenTeacherExists_ShouldReturnTeacher() {
        // Given
        Long courseId = 1L;
        when(teacherRepository.findByCourseId(courseId)).thenReturn(Optional.of(testTeacher));

        // When
        Optional<Teacher> result = teacherService.getTeacherByCourse(courseId);

        // Then
        assertThat(result).isPresent();
        verify(teacherRepository, times(1)).findByCourseId(courseId);
    }

    @Test
    void getTeacherByCourse_WhenNoTeacherExists_ShouldReturnEmpty() {
        // Given
        Long courseId = 999L;
        when(teacherRepository.findByCourseId(courseId)).thenReturn(Optional.empty());

        // When
        Optional<Teacher> result = teacherService.getTeacherByCourse(courseId);

        // Then
        assertThat(result).isEmpty();
        verify(teacherRepository, times(1)).findByCourseId(courseId);
    }

    @Test
    void saveTeacher_ShouldReturnSavedTeacher() {
        // Given
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // When
        Teacher result = teacherService.saveTeacher(testTeacher);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testTeacher.getId());
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    void deleteTeacher_ShouldCallRepository() {
        // Given
        Long teacherId = 1L;
        doNothing().when(teacherRepository).deleteById(teacherId);

        // When
        teacherService.deleteTeacher(teacherId);

        // Then
        verify(teacherRepository, times(1)).deleteById(teacherId);
    }

    @Test
    void updateTeacher_WhenTeacherExists_ShouldUpdateAndReturnTeacher() {
        // Given
        Long teacherId = 1L;
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setFirstName("Janet");
        updatedTeacher.setLastName("Johnson");
        updatedTeacher.setEmail("janet.johnson@example.com");
        updatedTeacher.setPhoneNumber("555-111-2222");
        updatedTeacher.setHireDate(LocalDate.of(2021, 3, 20));
        updatedTeacher.setSubjectSpecialty("Physics");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Teacher result = teacherService.updateTeacher(teacherId, updatedTeacher);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(teacherId);
        assertThat(result.getFirstName()).isEqualTo("Janet");
        assertThat(result.getLastName()).isEqualTo("Johnson");
        assertThat(result.getEmail()).isEqualTo("janet.johnson@example.com");
        assertThat(result.getPhoneNumber()).isEqualTo("555-111-2222");
        assertThat(result.getHireDate()).isEqualTo(LocalDate.of(2021, 3, 20));
        assertThat(result.getSubjectSpecialty()).isEqualTo("Physics");
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void updateTeacher_WhenTeacherDoesNotExist_ShouldCreateAndReturnTeacher() {
        // Given
        Long teacherId = 3L;
        Teacher newTeacher = new Teacher();
        newTeacher.setFirstName("Alex");
        newTeacher.setLastName("Brown");
        newTeacher.setEmail("alex.brown@example.com");
        newTeacher.setPhoneNumber("555-333-4444");
        newTeacher.setHireDate(LocalDate.of(2022, 5, 10));
        newTeacher.setSubjectSpecialty("Biology");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher savedTeacher = invocation.getArgument(0);
            return savedTeacher;
        });

        // When
        Teacher result = teacherService.updateTeacher(teacherId, newTeacher);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(teacherId);
        assertThat(result.getFirstName()).isEqualTo("Alex");
        assertThat(result.getLastName()).isEqualTo("Brown");
        assertThat(result.getEmail()).isEqualTo("alex.brown@example.com");
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }
}
