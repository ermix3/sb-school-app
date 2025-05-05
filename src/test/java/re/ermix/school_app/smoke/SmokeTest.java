package re.ermix.school_app.smoke;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import re.ermix.school_app.config.TestcontainersConfiguration;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.StudentRepository;
import re.ermix.school_app.repository.TeacherRepository;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Smoke tests for the School App.
 * These tests quickly verify that the main functionality works after deployments.
 * They should be fast and focus on critical paths rather than edge cases.
 */
@Tag("smoke")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public class SmokeTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;


    @Test
    @DisplayName("Smoke Test: Application context loads")
    void contextLoads() {
        // This test will fail if the application context cannot be loaded
        assertTrue(true, "Application context loaded successfully");
    }

    @Test
    @DisplayName("Smoke Test: Database connection works")
    void databaseConnectionWorks() {
        // Verify that we can connect to the database and perform basic operations
        long studentCount = studentRepository.count();
        long courseCount = courseRepository.count();
        long teacherCount = teacherRepository.count();

        // We don't care about the exact counts, just that the queries executed without errors
        assertTrue(studentCount >= 0, "Student repository is accessible");
        assertTrue(courseCount >= 0, "Course repository is accessible");
        assertTrue(teacherCount >= 0, "Teacher repository is accessible");
    }

    @Test
    @DisplayName("Smoke Test: Student API endpoints are accessible")
    void studentApiEndpointsAreAccessible() throws Exception {
        // Test that the student API endpoints are accessible
        mockMvc.perform(get("/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Smoke Test: Course API endpoints are accessible")
    void courseApiEndpointsAreAccessible() throws Exception {
        // Test that the course API endpoints are accessible
        mockMvc.perform(get("/courses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Smoke Test: Teacher API endpoints are accessible")
    void teacherApiEndpointsAreAccessible() throws Exception {
        // Test that the teacher API endpoints are accessible
        mockMvc.perform(get("/teachers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Smoke Test: Student search functionality works")
    void studentSearchFunctionalityWorks() throws Exception {
        // Test that the student search functionality works
        mockMvc.perform(get("/students/search")
                        .param("lastName", "")  // Empty parameter to match all students
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Smoke Test: Course search functionality works")
    void courseSearchFunctionalityWorks() throws Exception {
        // Test that the course search functionality works
        mockMvc.perform(get("/courses/search")
                        .param("title", "")  // Empty parameter to match all courses
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Smoke Test: Health endpoint is accessible")
    void healthEndpointIsAccessible() throws Exception {
        // Test that the health endpoint is accessible
        mockMvc.perform(get("/actuator/health")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}