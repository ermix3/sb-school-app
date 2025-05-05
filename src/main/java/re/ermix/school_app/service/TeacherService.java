package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ermix.school_app.model.Teacher;
import re.ermix.school_app.repository.TeacherRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {
    
    private final TeacherRepository teacherRepository;
    
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }
    
    public Optional<Teacher> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }
    
    public List<Teacher> getTeachersByLastName(String lastName) {
        return teacherRepository.findByLastName(lastName);
    }
    
    public List<Teacher> getTeachersByName(String firstName, String lastName) {
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
    }
    
    public List<Teacher> getTeachersBySubjectSpecialty(String subjectSpecialty) {
        return teacherRepository.findBySubjectSpecialty(subjectSpecialty);
    }
    
    public List<Teacher> getTeachersHiredAfter(LocalDate date) {
        return teacherRepository.findByHireDateAfter(date);
    }
    
    public Optional<Teacher> getTeacherByCourse(Long courseId) {
        return teacherRepository.findByCourseId(courseId);
    }
    
    @Transactional
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
    
    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        return teacherRepository.findById(id)
                .map(teacher -> {
                    teacher.setFirstName(teacherDetails.getFirstName());
                    teacher.setLastName(teacherDetails.getLastName());
                    teacher.setEmail(teacherDetails.getEmail());
                    teacher.setPhoneNumber(teacherDetails.getPhoneNumber());
                    teacher.setHireDate(teacherDetails.getHireDate());
                    teacher.setSubjectSpecialty(teacherDetails.getSubjectSpecialty());
                    return teacherRepository.save(teacher);
                })
                .orElseGet(() -> {
                    teacherDetails.setId(id);
                    return teacherRepository.save(teacherDetails);
                });
    }
}