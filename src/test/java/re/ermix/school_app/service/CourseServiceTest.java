package re.ermix.school_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Teacher;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.EnrollmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private List<Course> courseList;

    @BeforeEach
    void setUp() {
        // Set up test data
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setCourseCode("CS101");
        testCourse.setTitle("Introduction to Computer Science");
        testCourse.setDescription("An introductory course to CS");
        testCourse.setCredits(3);
        testCourse.setTeacher(teacher);
        testCourse.setMaxStudents(30);

        Course testCourse2 = new Course();
        testCourse2.setId(2L);
        testCourse2.setCourseCode("CS102");
        testCourse2.setTitle("Advanced Programming");
        testCourse2.setCredits(4);
        testCourse2.setTeacher(teacher);
        testCourse2.setMaxStudents(25);

        courseList = Arrays.asList(testCourse, testCourse2);
    }

    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        // Given
        when(courseRepository.findAll()).thenReturn(courseList);

        // When
        List<Course> result = courseService.getAllCourses();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(courseList);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_WhenCourseExists_ShouldReturnCourse() {
        // Given
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));

        // When
        Optional<Course> result = courseService.getCourseById(courseId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(courseId);
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void getCourseById_WhenCourseDoesNotExist_ShouldReturnEmpty() {
        // Given
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When
        Optional<Course> result = courseService.getCourseById(courseId);

        // Then
        assertThat(result).isEmpty();
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void getCourseByCourseCode_WhenCourseExists_ShouldReturnCourse() {
        // Given
        String courseCode = "CS101";
        when(courseRepository.findByCourseCode(courseCode)).thenReturn(Optional.of(testCourse));

        // When
        Optional<Course> result = courseService.getCourseByCourseCode(courseCode);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCourseCode()).isEqualTo(courseCode);
        verify(courseRepository, times(1)).findByCourseCode(courseCode);
    }

    @Test
    void getCoursesByTitle_ShouldReturnMatchingCourses() {
        // Given
        String title = "Introduction";
        when(courseRepository.findByTitle(title)).thenReturn(List.of(testCourse));

        // When
        List<Course> result = courseService.getCoursesByTitle(title);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains(title);
        verify(courseRepository, times(1)).findByTitle(title);
    }

    @Test
    void getCoursesByCredits_ShouldReturnMatchingCourses() {
        // Given
        Integer credits = 3;
        when(courseRepository.findByCredits(credits)).thenReturn(List.of(testCourse));

        // When
        List<Course> result = courseService.getCoursesByCredits(credits);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCredits()).isEqualTo(credits);
        verify(courseRepository, times(1)).findByCredits(credits);
    }

    @Test
    void getCoursesByTeacher_ShouldReturnMatchingCourses() {
        // Given
        Long teacherId = 1L;
        when(courseRepository.findByTeacherId(teacherId)).thenReturn(courseList);

        // When
        List<Course> result = courseService.getCoursesByTeacher(teacherId);

        // Then
        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findByTeacherId(teacherId);
    }

    @Test
    void getCoursesWithAvailableSeats_ShouldReturnAvailableCourses() {
        // Given
        when(courseRepository.findCoursesWithAvailableSeats()).thenReturn(courseList);

        // When
        List<Course> result = courseService.getCoursesWithAvailableSeats();

        // Then
        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findCoursesWithAvailableSeats();
    }

    @Test
    void getCoursesByTeacherSpecialty_ShouldReturnMatchingCourses() {
        // Given
        String specialty = "Computer Science";
        when(courseRepository.findByTeacherSpecialty(specialty)).thenReturn(courseList);

        // When
        List<Course> result = courseService.getCoursesByTeacherSpecialty(specialty);

        // Then
        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findByTeacherSpecialty(specialty);
    }

    @Test
    void getCoursesByStudent_ShouldReturnEnrolledCourses() {
        // Given
        Long studentId = 1L;
        when(courseRepository.findByStudentId(studentId)).thenReturn(courseList);

        // When
        List<Course> result = courseService.getCoursesByStudent(studentId);

        // Then
        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    void isCourseAvailable_WhenCourseNotFound_ShouldReturnFalse() {
        // Given
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When
        boolean result = courseService.isCourseAvailable(courseId);

        // Then
        assertThat(result).isFalse();
        verify(courseRepository, times(1)).findById(courseId);
        verifyNoInteractions(enrollmentRepository);
    }

    @Test
    void isCourseAvailable_WhenCourseHasNoLimit_ShouldReturnTrue() {
        // Given
        Long courseId = 1L;
        testCourse.setMaxStudents(null);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));

        // When
        boolean result = courseService.isCourseAvailable(courseId);

        // Then
        assertThat(result).isTrue();
        verify(courseRepository, times(1)).findById(courseId);
        verifyNoInteractions(enrollmentRepository);
    }

    @Test
    void isCourseAvailable_WhenEnrollmentsLessThanMax_ShouldReturnTrue() {
        // Given
        Long courseId = 1L;
        testCourse.setMaxStudents(30);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.countActiveByCourseId(courseId)).thenReturn(25L);

        // When
        boolean result = courseService.isCourseAvailable(courseId);

        // Then
        assertThat(result).isTrue();
        verify(courseRepository, times(1)).findById(courseId);
        verify(enrollmentRepository, times(1)).countActiveByCourseId(courseId);
    }

    @Test
    void isCourseAvailable_WhenEnrollmentsEqualToMax_ShouldReturnFalse() {
        // Given
        Long courseId = 1L;
        testCourse.setMaxStudents(30);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.countActiveByCourseId(courseId)).thenReturn(30L);

        // When
        boolean result = courseService.isCourseAvailable(courseId);

        // Then
        assertThat(result).isFalse();
        verify(courseRepository, times(1)).findById(courseId);
        verify(enrollmentRepository, times(1)).countActiveByCourseId(courseId);
    }

    @Test
    void saveCourse_ShouldReturnSavedCourse() {
        // Given
        when(courseRepository.save(testCourse)).thenReturn(testCourse);

        // When
        Course result = courseService.saveCourse(testCourse);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCourse.getId());
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void deleteCourse_ShouldCallRepository() {
        // Given
        Long courseId = 1L;
        doNothing().when(courseRepository).deleteById(courseId);

        // When
        courseService.deleteCourse(courseId);

        // Then
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void updateCourse_WhenCourseExists_ShouldUpdateAndReturnCourse() {
        // Given
        Long courseId = 1L;
        Course updatedCourse = new Course();
        updatedCourse.setCourseCode("CS999");
        updatedCourse.setTitle("Updated Course");
        updatedCourse.setDescription("Updated Description");
        updatedCourse.setCredits(5);
        updatedCourse.setMaxStudents(40);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Course result = courseService.updateCourse(courseId, updatedCourse);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(courseId);
        assertThat(result.getCourseCode()).isEqualTo("CS999");
        assertThat(result.getTitle()).isEqualTo("Updated Course");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getCredits()).isEqualTo(5);
        assertThat(result.getMaxStudents()).isEqualTo(40);
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourse_WhenCourseDoesNotExist_ShouldCreateAndReturnCourse() {
        // Given
        Long courseId = 999L;
        Course newCourse = new Course();
        newCourse.setCourseCode("CS999");
        newCourse.setTitle("New Course");
        newCourse.setCredits(5);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course savedCourse = invocation.getArgument(0);
            return savedCourse;
        });

        // When
        Course result = courseService.updateCourse(courseId, newCourse);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(courseId);
        assertThat(result.getCourseCode()).isEqualTo("CS999");
        assertThat(result.getTitle()).isEqualTo("New Course");
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
}
