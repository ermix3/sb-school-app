package re.ermix.school_app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchCriteria {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate enrollmentDate;
    private LocalDate dateOfBirthStart;
    private LocalDate dateOfBirthEnd;
    private Long courseId;
}
