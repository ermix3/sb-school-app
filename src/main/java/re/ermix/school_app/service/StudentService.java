package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.model.StudentSearchCriteria;
import re.ermix.school_app.repository.StudentRepository;
import re.ermix.school_app.specification.StudentSpecification;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> searchStudents(StudentSearchCriteria criteria) {
        Specification<Student> spec = StudentSpecification.buildSpecification(criteria);
        return studentRepository.findAll(spec);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setAddress(updatedStudent.getAddress());
        existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
        existingStudent.setEnrollmentDate(updatedStudent.getEnrollmentDate());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}