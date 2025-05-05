Feature: Student Enrollment
  As a school administrator
  I want to enroll students in courses
  So that they can attend classes and receive grades

  Background:
    Given the following students exist:
      | firstName | lastName | email                | dateOfBirth | enrollmentDate |
      | John      | Doe      | john.doe@example.com | 2000-01-01  | 2023-09-01     |
      | Jane      | Smith    | jane.smith@example.com | 1999-05-15 | 2023-09-01     |
    And the following courses exist:
      | courseCode | title          | credits |
      | MATH101    | Mathematics    | 3       |
      | PHYS101    | Physics        | 4       |
      | COMP101    | Computer Science | 3     |

  Scenario: Successfully enroll a student in a course
    Given I am logged in as an administrator
    When I enroll student "John Doe" in course "MATH101"
    Then the enrollment should be successful
    And student "John Doe" should be enrolled in course "MATH101"
    And the enrollment status should be "ACTIVE"

  Scenario: Attempt to enroll a student in a course they are already enrolled in
    Given I am logged in as an administrator
    And student "Jane Smith" is already enrolled in course "PHYS101"
    When I enroll student "Jane Smith" in course "PHYS101"
    Then the enrollment should fail
    And I should see an error message indicating duplicate enrollment

  Scenario: Enroll a student in multiple courses
    Given I am logged in as an administrator
    When I enroll student "John Doe" in course "MATH101"
    And I enroll student "John Doe" in course "PHYS101"
    And I enroll student "John Doe" in course "COMP101"
    Then student "John Doe" should be enrolled in 3 courses
    And the courses should include "MATH101", "PHYS101", and "COMP101"

  Scenario: Drop a student from a course
    Given I am logged in as an administrator
    And student "Jane Smith" is already enrolled in course "PHYS101"
    When I drop student "Jane Smith" from course "PHYS101"
    Then student "Jane Smith" should not be enrolled in course "PHYS101"
    And the enrollment record should be removed

  Scenario Outline: Enroll students with different statuses
    Given I am logged in as an administrator
    When I enroll student "<student>" in course "<course>" with status "<status>"
    Then the enrollment should be successful
    And student "<student>" should be enrolled in course "<course>"
    And the enrollment status should be "<status>"

    Examples:
      | student    | course  | status    |
      | John Doe   | MATH101 | ACTIVE    |
      | Jane Smith | PHYS101 | WAITLISTED |
      | John Doe   | COMP101 | PENDING   |