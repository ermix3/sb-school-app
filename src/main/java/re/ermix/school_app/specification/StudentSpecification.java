package re.ermix.school_app.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import re.ermix.school_app.model.Student;
import re.ermix.school_app.model.StudentSearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> buildSpecification(StudentSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("email"), criteria.getEmail()));
            }

            if (criteria.getFirstName() != null && !criteria.getFirstName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        "%" + criteria.getFirstName().toLowerCase() + "%"));
            }

            if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                        "%" + criteria.getLastName().toLowerCase() + "%"));
            }

            if (criteria.getEnrollmentDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("enrollmentDate"), criteria.getEnrollmentDate()));
            }

            if (criteria.getDateOfBirthStart() != null && criteria.getDateOfBirthEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("dateOfBirth"), 
                    criteria.getDateOfBirthStart(), criteria.getDateOfBirthEnd()));
            }

            if (criteria.getCourseId() != null) {
                Join<Object, Object> enrollmentsJoin = root.join("enrollments");
                Join<Object, Object> courseJoin = enrollmentsJoin.join("course");
                predicates.add(criteriaBuilder.equal(courseJoin.get("id"), criteria.getCourseId()));
                
                // Ensure distinct results when joining
                assert query != null;
                query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
