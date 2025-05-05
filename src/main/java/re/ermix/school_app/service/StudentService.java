package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
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
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    public List<Student> getStudentsByLastName(String lastName) {
        return studentRepository.findByLastName(lastName);
    }
    
    public List<Student> getStudentsByName(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName);
    }
    
    public List<Student> getStudentsByEnrollmentDate(LocalDate enrollmentDate) {
        return studentRepository.findByEnrollmentDate(enrollmentDate);
    }
    
    public List<Student> getStudentsByDateOfBirthRange(LocalDate startDate, LocalDate endDate) {
        return studentRepository.findByDateOfBirthBetween(startDate, endDate);
    }
    
    public List<Student> getStudentsByCourse(Long courseId) {
        return studentRepository.findByCourseId(courseId);
    }
    
    @Transactional
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(studentDetails.getFirstName());
                    student.setLastName(studentDetails.getLastName());
                    student.setEmail(studentDetails.getEmail());
                    student.setDateOfBirth(studentDetails.getDateOfBirth());
                    student.setAddress(studentDetails.getAddress());
                    student.setPhoneNumber(studentDetails.getPhoneNumber());
                    student.setEnrollmentDate(studentDetails.getEnrollmentDate());
                    return studentRepository.save(student);
                })
                .orElseGet(() -> {
                    studentDetails.setId(id);
                    return studentRepository.save(studentDetails);
                });
    }
}