package re.ermix.school_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> getAllTeachers() {
        log.info("Get all teachers");
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        log.info("Get teacher by id: {}", id);
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getTeacherByEmail(String email) {
        log.info("Get teacher by email: {}", email);
        return teacherRepository.findByEmail(email);
    }

    public List<Teacher> getTeachersByLastName(String lastName) {
        log.info("Get teachers by last name: {}", lastName);
        return teacherRepository.findByLastName(lastName);
    }

    public List<Teacher> getTeachersByName(String firstName, String lastName) {
        log.info("Get teachers by name: {} {}", firstName, lastName);
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Teacher> getTeachersBySubjectSpecialty(String subjectSpecialty) {
        log.info("Get teachers by subject specialty: {}", subjectSpecialty);
        return teacherRepository.findBySubjectSpecialty(subjectSpecialty);
    }

    public List<Teacher> getTeachersHiredAfter(LocalDate date) {
        log.info("Get teachers hired after: {}", date);
        return teacherRepository.findByHireDateAfter(date);
    }

    public Optional<Teacher> getTeacherByCourse(Long courseId) {
        log.info("Get teacher by course id: {}", courseId);
        return teacherRepository.findByCourseId(courseId);
    }

    @Transactional
    public Teacher saveTeacher(Teacher teacher) {
        log.info("Save teacher: {} {}", teacher.getFirstName(), teacher.getLastName());
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        log.info("Delete teacher with id: {}", id);
        teacherRepository.deleteById(id);
    }

    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        log.info("Update teacher with id: {}", id);
        return teacherRepository.findById(id)
                .map(teacher -> {
                    teacher.setFirstName(teacherDetails.getFirstName());
                    teacher.setLastName(teacherDetails.getLastName());
                    teacher.setEmail(teacherDetails.getEmail());
                    teacher.setPhoneNumber(teacherDetails.getPhoneNumber());
                    teacher.setHireDate(teacherDetails.getHireDate());
                    teacher.setSubjectSpecialty(teacherDetails.getSubjectSpecialty());
                    log.info("Updating existing teacher: {} {}", teacher.getFirstName(), teacher.getLastName());
                    return teacherRepository.save(teacher);
                })
                .orElseGet(() -> {
                    teacherDetails.setId(id);
                    log.info("Creating new teacher with id: {}", id);
                    return teacherRepository.save(teacherDetails);
                });
    }
}
