package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        log.info("Get all students");
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        log.info("Get student by id: {}", id);
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByEmail(String email) {
        log.info("Get student by email: {}", email);
        return studentRepository.findByEmail(email);
    }

    public List<Student> getStudentsByLastName(String lastName) {
        log.info("Get students by last name: {}", lastName);
        return studentRepository.findByLastName(lastName);
    }

    public List<Student> getStudentsByName(String firstName, String lastName) {
        log.info("Get students by name: {} {}", firstName, lastName);
        return studentRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Student> getStudentsByEnrollmentDate(LocalDate enrollmentDate) {
        log.info("Get students by enrollment date: {}", enrollmentDate);
        return studentRepository.findByEnrollmentDate(enrollmentDate);
    }

    public List<Student> getStudentsByDateOfBirthRange(LocalDate startDate, LocalDate endDate) {
        log.info("Get students by date of birth range: {} to {}", startDate, endDate);
        return studentRepository.findByDateOfBirthBetween(startDate, endDate);
    }

    public List<Student> getStudentsByCourse(Long courseId) {
        log.info("Get students by course id: {}", courseId);
        return studentRepository.findByCourseId(courseId);
    }

    @Transactional
    public Student saveStudent(Student student) {
        log.info("Save student: {} {}", student.getFirstName(), student.getLastName());
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        log.info("Delete student with id: {}", id);
        studentRepository.deleteById(id);
    }

    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        log.info("Update student with id: {}", id);
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(studentDetails.getFirstName());
                    student.setLastName(studentDetails.getLastName());
                    student.setEmail(studentDetails.getEmail());
                    student.setDateOfBirth(studentDetails.getDateOfBirth());
                    student.setAddress(studentDetails.getAddress());
                    student.setPhoneNumber(studentDetails.getPhoneNumber());
                    student.setEnrollmentDate(studentDetails.getEnrollmentDate());
                    log.info("Updating existing student: {} {}", student.getFirstName(), student.getLastName());
                    return studentRepository.save(student);
                })
                .orElseGet(() -> {
                    studentDetails.setId(id);
                    log.info("Creating new student with id: {}", id);
                    return studentRepository.save(studentDetails);
                });
    }
}
