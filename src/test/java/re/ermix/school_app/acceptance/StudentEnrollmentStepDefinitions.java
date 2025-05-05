package re.ermix.school_app.acceptance;

//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.Before;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import re.ermix.school_app.TestSchoolAppApplication;
//import re.ermix.school_app.model.Course;
//import re.ermix.school_app.model.Enrollment;
//import re.ermix.school_app.model.Student;
//import re.ermix.school_app.service.CourseService;
//import re.ermix.school_app.service.EnrollmentService;
//import re.ermix.school_app.service.StudentService;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = TestSchoolAppApplication.class)
//@ActiveProfiles("test")
//@Transactional
public class StudentEnrollmentStepDefinitions {

//    @Autowired
//    private StudentService studentService;
//
//    @Autowired
//    private CourseService courseService;
//
//    @Autowired
//    private EnrollmentService enrollmentService;
//
//    private Map<String, Student> students = new HashMap<>();
//    private Map<String, Course> courses = new HashMap<>();
//    private boolean isLoggedInAsAdmin = false;
//    private boolean enrollmentSuccess = false;
//    private String errorMessage = null;
//
//    @Before
//    public void setup() {
//        students.clear();
//        courses.clear();
//        isLoggedInAsAdmin = false;
//        enrollmentSuccess = false;
//        errorMessage = null;
//    }
//
//    @Given("the following students exist:")
//    public void theFollowingStudentsExist(DataTable dataTable) {
//        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
//
//        for (Map<String, String> row : rows) {
//            Student student = new Student();
//            student.setFirstName(row.get("firstName"));
//            student.setLastName(row.get("lastName"));
//            student.setEmail(row.get("email"));
//            student.setDateOfBirth(LocalDate.parse(row.get("dateOfBirth")));
//            student.setEnrollmentDate(LocalDate.parse(row.get("enrollmentDate")));
//
//            student = studentService.saveStudent(student);
//            students.put(student.getFirstName() + " " + student.getLastName(), student);
//        }
//    }
//
//    @And("the following courses exist:")
//    public void theFollowingCoursesExist(DataTable dataTable) {
//        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
//
//        for (Map<String, String> row : rows) {
//            Course course = new Course();
//            course.setCourseCode(row.get("courseCode"));
//            course.setTitle(row.get("title"));
//            course.setCredits(Integer.parseInt(row.get("credits")));
//
//            course = courseService.saveCourse(course);
//            courses.put(course.getCourseCode(), course);
//        }
//    }
//
//    @Given("I am logged in as an administrator")
//    public void iAmLoggedInAsAnAdministrator() {
//        isLoggedInAsAdmin = true;
//    }
//
//    @And("student {string} is already enrolled in course {string}")
//    public void studentIsAlreadyEnrolledInCourse(String studentName, String courseCode) {
//        Student student = students.get(studentName);
//        Course course = courses.get(courseCode);
//
//        assertNotNull(student, "Student not found: " + studentName);
//        assertNotNull(course, "Course not found: " + courseCode);
//
//        try {
//            Enrollment enrollment = new Enrollment();
//            enrollment.setEnrollmentDate(LocalDate.now());
//            enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);
//
//            enrollmentService.enrollStudentInCourse(student.getId(), course.getId(), enrollment);
//        } catch (Exception e) {
//            fail("Failed to set up enrollment: " + e.getMessage());
//        }
//    }
//
//    @When("I enroll student {string} in course {string}")
//    public void iEnrollStudentInCourse(String studentName, String courseCode) {
//        Student student = students.get(studentName);
//        Course course = courses.get(courseCode);
//
//        assertNotNull(student, "Student not found: " + studentName);
//        assertNotNull(course, "Course not found: " + courseCode);
//
//        try {
//            Enrollment enrollment = new Enrollment();
//            enrollment.setEnrollmentDate(LocalDate.now());
//            enrollment.setStatus(EnrollmentStatusEnum.ACTIVE);
//
//            enrollmentService.enrollStudentInCourse(student.getId(), course.getId(), enrollment);
//            enrollmentSuccess = true;
//        } catch (Exception e) {
//            enrollmentSuccess = false;
//            errorMessage = e.getMessage();
//        }
//    }
//
//    @When("I enroll student {string} in course {string} with status {string}")
//    public void iEnrollStudentInCourseWithStatus(String studentName, String courseCode, String status) {
//        Student student = students.get(studentName);
//        Course course = courses.get(courseCode);
//
//        assertNotNull(student, "Student not found: " + studentName);
//        assertNotNull(course, "Course not found: " + courseCode);
//
//        try {
//            Enrollment enrollment = new Enrollment();
//            enrollment.setEnrollmentDate(LocalDate.now());
//            enrollment.setStatus(EnrollmentStatusEnum.valueOf(status));
//
//            enrollmentService.enrollStudentInCourse(student.getId(), course.getId(), enrollment);
//            enrollmentSuccess = true;
//        } catch (Exception e) {
//            enrollmentSuccess = false;
//            errorMessage = e.getMessage();
//        }
//    }
//
//    @When("I drop student {string} from course {string}")
//    public void iDropStudentFromCourse(String studentName, String courseCode) {
//        Student student = students.get(studentName);
//        Course course = courses.get(courseCode);
//
//        assertNotNull(student, "Student not found: " + studentName);
//        assertNotNull(course, "Course not found: " + courseCode);
//
//        try {
//            Optional<Enrollment> enrollmentOpt = enrollmentService.getEnrollmentByStudentAndCourse(
//                    student.getId(), course.getId());
//
//            assertTrue(enrollmentOpt.isPresent(), "Enrollment not found");
//
//            enrollmentService.deleteEnrollment(enrollmentOpt.get().getId());
//            enrollmentSuccess = true;
//        } catch (Exception e) {
//            enrollmentSuccess = false;
//            errorMessage = e.getMessage();
//        }
//    }
//
//    @Then("the enrollment should be successful")
//    public void theEnrollmentShouldBeSuccessful() {
//        assertTrue(enrollmentSuccess, "Enrollment failed: " + errorMessage);
//    }
//
//    @Then("the enrollment should fail")
//    public void theEnrollmentShouldFail() {
//        assertFalse(enrollmentSuccess, "Enrollment succeeded when it should have failed");
//    }
//
//    @And("I should see an error message indicating duplicate enrollment")
//    public void iShouldSeeAnErrorMessageIndicatingDuplicateEnrollment() {
//        assertNotNull(errorMessage, "No error message was provided");
//        assertTrue(errorMessage.toLowerCase().contains("already enrolled") ||
//                   errorMessage.toLowerCase().contains("duplicate"),
//                   "Error message does not indicate duplicate enrollment: " + errorMessage);
//    }
//
//    @And("student {string} should be enrolled in course {string}")
//    public void studentShouldBeEnrolledInCourse(String studentName, String courseCode) {
//        Student student = students.get(studentName);
//        Course course = courses.get(courseCode);
//
//        assertNotNull(student, "Student not found: " + studentName);
//        assertNotNull(course, "Course not found: " + courseCode);
//
//        Optional<Enrollment> enrollmentOpt = enrollmentService.getEnrollmentByStudentAndCourse(
//                student.getId(), course.getId());
//
//        assertTrue(enrollmentOpt.isPresent(), "Student is not enrolled in the course");
//    }
//
//    @And("student {string} should not be enrolled in course {string}")
//    public void studentShouldNotBeEnrolledInCourse(String studentName, String courseCode) {
//        Student student = students.get(studentName);
//        Course course = courses.get(courseCode);
//
//        assertNotNull(student, "Student not found: " + studentName);
//        assertNotNull(course, "Course not found: " + courseCode);
//
//        Optional<Enrollment> enrollmentOpt = enrollmentService.getEnrollmentByStudentAndCourse(
//                student.getId(), course.getId());
//
//        assertFalse(enrollmentOpt.isPresent(), "Student is still enrolled in the course");
//    }
//
//    @And("the enrollment status should be {string}")
//    public void theEnrollmentStatusShouldBe(String status) {
//        // This assumes the last operation was an enrollment
//        // In a real implementation, you would track the last enrollment created
//        List<Enrollment> allEnrollments = enrollmentService.getAllEnrollments();
//        assertFalse(allEnrollments.isEmpty(), "No enrollments found");
//
//        Enrollment lastEnrollment = allEnrollments.get(allEnrollments.size() - 1);
//        assertEquals(EnrollmentStatusEnum.valueOf(status), lastEnrollment.getStatus(),
//                "Enrollment status does not match expected value");
//    }
//
//    @Then("student {string} should be enrolled in {int} courses")
//    public void studentShouldBeEnrolledInCourses(String studentName, int courseCount) {
//        Student student = students.get(studentName);
//        assertNotNull(student, "Student not found: " + studentName);
//
//        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getId());
//        assertEquals(courseCount, enrollments.size(),
//                "Student is enrolled in " + enrollments.size() + " courses, expected " + courseCount);
//    }
//
//    @And("the courses should include {string}, {string}, and {string}")
//    public void theCoursesShouldIncludeAnd(String course1, String course2, String course3) {
//        Student student = students.get("John Doe"); // Assuming this is for John Doe as per the scenario
//        assertNotNull(student, "Student not found: John Doe");
//
//        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getId());
//
//        boolean hasCourse1 = false;
//        boolean hasCourse2 = false;
//        boolean hasCourse3 = false;
//
//        for (Enrollment enrollment : enrollments) {
//            String courseCode = enrollment.getCourse().getCourseCode();
//            if (courseCode.equals(course1)) hasCourse1 = true;
//            if (courseCode.equals(course2)) hasCourse2 = true;
//            if (courseCode.equals(course3)) hasCourse3 = true;
//        }
//
//        assertTrue(hasCourse1, "Student is not enrolled in course: " + course1);
//        assertTrue(hasCourse2, "Student is not enrolled in course: " + course2);
//        assertTrue(hasCourse3, "Student is not enrolled in course: " + course3);
//    }
//
//    @And("the enrollment record should be removed")
//    public void theEnrollmentRecordShouldBeRemoved() {
//        // This is already verified in studentShouldNotBeEnrolledInCourse
//        assertTrue(true);
//    }
}