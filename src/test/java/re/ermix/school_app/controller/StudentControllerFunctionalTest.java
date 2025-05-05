package re.ermix.school_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.TestSchoolAppApplication;
import re.ermix.school_app.model.Course;
import re.ermix.school_app.model.Enrollment;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.repository.CourseRepository;
import re.ermix.school_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Functional tests for the StudentController.
 * These tests verify the complete student management workflow through the API.
 */

@Disabled
@Transactional
@AllArgsConstructor
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = TestSchoolAppApplication.class)
public class StudentControllerFunctionalTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Create a test student
        testStudent = new Student();
        testStudent.setFirstName("Functional");
        testStudent.setLastName("Test");
        testStudent.setEmail("functional.test@example.com");
        testStudent.setDateOfBirth(LocalDate.of(2000, 1, 1));
        testStudent.setAddress("123 Test St");
        testStudent.setPhoneNumber("555-123-4567");
        testStudent.setEnrollmentDate(LocalDate.now());
        testStudent = studentRepository.save(testStudent);

        // Create a test course
        testCourse = new Course();
        testCourse.setCourseCode("FUNC101");
        testCourse.setTitle("Functional Testing");
        testCourse.setDescription("Learn functional testing techniques");
        testCourse.setCredits(3);
        testCourse = courseRepository.save(testCourse);
    }

    @Test
    void testCompleteStudentLifecycle() throws Exception {
        // 1. Create a new student
        Map<String, Object> newStudentMap = new HashMap<>();
        newStudentMap.put("firstName", "New");
        newStudentMap.put("lastName", "Student");
        newStudentMap.put("email", "new.student@example.com");
        newStudentMap.put("dateOfBirth", "2001-02-15");
        newStudentMap.put("address", "456 New St");
        newStudentMap.put("phoneNumber", "555-987-6543");
        newStudentMap.put("enrollmentDate", LocalDate.now().toString());

        MvcResult createResult = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentMap)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("New")))
                .andExpect(jsonPath("$.lastName", is("Student")))
                .andReturn();

        // Extract the created student ID
        String responseJson = createResult.getResponse().getContentAsString();
        Student createdStudent = objectMapper.readValue(responseJson, Student.class);
        Long studentId = createdStudent.getId();

        // 2. Get the student by ID
        mockMvc.perform(get("/api/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.email", is("new.student@example.com")));

        // 3. Update the student
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("firstName", "Updated");
        updateMap.put("lastName", "Student");
        updateMap.put("email", "updated.student@example.com");
        updateMap.put("dateOfBirth", "2001-02-15");
        updateMap.put("address", "789 Update St");
        updateMap.put("phoneNumber", "555-111-2222");
        updateMap.put("enrollmentDate", LocalDate.now().toString());

        mockMvc.perform(put("/api/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.address", is("789 Update St")));

        // 4. Enroll the student in a course
        Map<String, Object> enrollmentMap = new HashMap<>();
        enrollmentMap.put("courseId", testCourse.getId());
        enrollmentMap.put("enrollmentDate", LocalDate.now().toString());

        mockMvc.perform(post("/api/students/{id}/enrollments", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentMap)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.course.id", is(testCourse.getId().intValue())))
                .andExpect(jsonPath("$.status", is("ACTIVE")));

        // 5. Get student's enrollments
        mockMvc.perform(get("/api/students/{id}/enrollments", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].course.courseCode", is("FUNC101")));

        // 6. Search for students by last name
        mockMvc.perform(get("/api/students/search")
                        .param("lastName", "Student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].lastName", is("Student")));

        // 7. Delete the student
        mockMvc.perform(delete("/api/students/{id}", studentId))
                .andExpect(status().isNoContent());

        // 8. Verify the student is deleted
        mockMvc.perform(get("/api/students/{id}", studentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testStudentValidation() throws Exception {
        // Test creating a student with missing required fields
        Map<String, Object> invalidStudent = new HashMap<>();
        invalidStudent.put("firstName", "Invalid");
        // Missing lastName, email, dateOfBirth, and enrollmentDate

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());

        // Test creating a student with invalid email format
        Map<String, Object> invalidEmailStudent = new HashMap<>();
        invalidEmailStudent.put("firstName", "Invalid");
        invalidEmailStudent.put("lastName", "Email");
        invalidEmailStudent.put("email", "not-an-email");
        invalidEmailStudent.put("dateOfBirth", "2000-01-01");
        invalidEmailStudent.put("enrollmentDate", LocalDate.now().toString());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmailStudent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDuplicateEmailHandling() throws Exception {
        // Try to create a student with an email that already exists
        Map<String, Object> duplicateEmailStudent = new HashMap<>();
        duplicateEmailStudent.put("firstName", "Duplicate");
        duplicateEmailStudent.put("lastName", "Email");
        duplicateEmailStudent.put("email", "functional.test@example.com"); // Same as testStudent
        duplicateEmailStudent.put("dateOfBirth", "2000-01-01");
        duplicateEmailStudent.put("enrollmentDate", LocalDate.now().toString());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmailStudent)))
                .andExpect(status().isConflict());
    }

    @Test
    void testStudentFiltering() throws Exception {
        // Test filtering by enrollment date
        mockMvc.perform(get("/api/students/filter")
                        .param("enrollmentDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].enrollmentDate", is(LocalDate.now().toString())));

        // Test filtering by date of birth range
        mockMvc.perform(get("/api/students/filter")
                        .param("startDate", "1999-01-01")
                        .param("endDate", "2001-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].dateOfBirth", is("2000-01-01")));
    }

    @Test
    void testCourseEnrollmentAndWithdrawal() throws Exception {
        // 1. Enroll the test student in the test course
        Map<String, Object> enrollmentMap = new HashMap<>();
        enrollmentMap.put("courseId", testCourse.getId());
        enrollmentMap.put("enrollmentDate", LocalDate.now().toString());

        MvcResult enrollResult = mockMvc.perform(post("/api/students/{id}/enrollments", testStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentMap)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.course.id", is(testCourse.getId().intValue())))
                .andReturn();

        // Extract the enrollment ID
        String enrollResponseJson = enrollResult.getResponse().getContentAsString();
        Enrollment enrollment = objectMapper.readValue(enrollResponseJson, Enrollment.class);
        Long enrollmentId = enrollment.getId();

        // 2. Verify the enrollment exists
        mockMvc.perform(get("/api/students/{id}/enrollments", testStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(enrollmentId.intValue())));

        // 3. Update the enrollment status to DROPPED
        Map<String, Object> updateStatusMap = new HashMap<>();
        updateStatusMap.put("status", "DROPPED");

        mockMvc.perform(put("/api/students/{studentId}/enrollments/{enrollmentId}", testStudent.getId(), enrollmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStatusMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DROPPED")));

        // 4. Delete the enrollment
        mockMvc.perform(delete("/api/students/{studentId}/enrollments/{enrollmentId}", testStudent.getId(), enrollmentId))
                .andExpect(status().isNoContent());

        // 5. Verify the enrollment is deleted
        mockMvc.perform(get("/api/students/{id}/enrollments", testStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}