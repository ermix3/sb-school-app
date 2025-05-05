# School App Testing Guide

This document provides instructions on how to run the various types of tests implemented for the School App project.

## Table of Contents
1. [Unit Tests](#unit-tests)
2. [Integration Tests](#integration-tests)
3. [Functional Tests](#functional-tests)
4. [End-to-End Tests](#end-to-end-tests)
5. [Acceptance Tests](#acceptance-tests)
6. [Performance Tests](#performance-tests)
7. [Smoke Tests](#smoke-tests)
8. [Test Coverage](#test-coverage)

## Unit Tests

Unit tests verify the functionality of individual components in isolation.

### Running Unit Tests

```bash
# Run all unit tests
mvn test

# Run a specific test class
mvn test -Dtest=StudentServiceTest

# Run a specific test method
mvn test -Dtest=StudentServiceTest#testAddEnrollment
```

### Key Unit Test Classes
- `StudentServiceTest`: Tests for the StudentService class methods
- `StudentTest`: Tests for the Student model class methods

## Integration Tests

Integration tests verify that different components work together correctly.

### Running Integration Tests

```bash
# Run all integration tests
mvn test -Dtest=*IntegrationTest

# Run a specific integration test
mvn test -Dtest=CourseServiceIntegrationTest
```

### Key Integration Test Classes
- `CourseServiceIntegrationTest`: Tests for the CourseService class with actual database interactions

## Functional Tests

Functional tests verify that the system meets functional requirements through the API.

### Running Functional Tests

```bash
# Run all functional tests
mvn test -Dtest=*FunctionalTest

# Run a specific functional test
mvn test -Dtest=StudentControllerFunctionalTest
```

### Key Functional Test Classes
- `StudentControllerFunctionalTest`: Tests for the complete student management workflow through the API

## End-to-End Tests

End-to-End tests verify the entire application works correctly from the user's perspective.

### Running End-to-End Tests

```bash
# Run all E2E tests
mvn test -Dtest=*E2ETest

# Note: To run E2E tests with Selenium, you need to have the appropriate WebDriver installed
```

### Setting Up E2E Tests
1. Add Selenium dependencies to pom.xml:
   ```xml
   <dependency>
       <groupId>org.seleniumhq.selenium</groupId>
       <artifactId>selenium-java</artifactId>
       <version>4.10.0</version>
       <scope>test</scope>
   </dependency>
   <dependency>
       <groupId>io.github.bonigarcia</groupId>
       <artifactId>webdrivermanager</artifactId>
       <version>5.3.3</version>
       <scope>test</scope>
   </dependency>
   ```

2. Create E2E test classes in the `e2e` package

## Acceptance Tests

Acceptance tests verify that the system meets business requirements using behavior-driven development (BDD).

### Setting Up Acceptance Tests
1. Add Cucumber dependencies to pom.xml:
   ```xml
   <dependency>
       <groupId>io.cucumber</groupId>
       <artifactId>cucumber-java</artifactId>
       <version>7.12.1</version>
       <scope>test</scope>
   </dependency>
   <dependency>
       <groupId>io.cucumber</groupId>
       <artifactId>cucumber-spring</artifactId>
       <version>7.12.1</version>
       <scope>test</scope>
   </dependency>
   <dependency>
       <groupId>io.cucumber</groupId>
       <artifactId>cucumber-junit</artifactId>
       <version>7.12.1</version>
       <scope>test</scope>
   </dependency>
   ```

2. Create a Cucumber runner class:
   ```java
   @RunWith(Cucumber.class)
   @CucumberOptions(
       features = "src/test/resources/features",
       glue = "re.ermix.school_app.acceptance",
       plugin = {"pretty", "html:target/cucumber-reports"}
   )
   public class CucumberTestRunner {
   }
   ```

3. Run the acceptance tests:
   ```bash
   mvn test -Dtest=CucumberTestRunner
   ```

### Key Acceptance Test Files
- `src/test/resources/features/student_enrollment.feature`: Feature file for student enrollment
- `StudentEnrollmentStepDefinitions.java`: Step definitions for the student enrollment feature

## Performance Tests

Performance tests verify the system's responsiveness, scalability, and stability under load.

### Running Performance Tests
1. Install Apache JMeter from https://jmeter.apache.org/download_jmeter.cgi
2. Open JMeter and load the test plan:
   ```bash
   jmeter -t src/test/resources/jmeter/school-app-performance-test.jmx
   ```
3. Configure the test parameters (host, port, etc.)
4. Run the test and analyze the results

### Key Performance Test Files
- `src/test/resources/jmeter/school-app-performance-test.jmx`: JMeter test plan for performance testing

## Smoke Tests

Smoke tests quickly verify that the main functionality works after deployments.

### Running Smoke Tests

```bash
# Run all smoke tests
mvn test -Dgroups=smoke

# Run a specific smoke test class
mvn test -Dtest=SmokeTestSuite
```

### Key Smoke Test Classes
- `SmokeTestSuite`: Basic smoke tests for the application

## Test Coverage

To generate a test coverage report:

```bash
mvn clean verify jacoco:report
```

The report will be available at `target/site/jacoco/index.html`.

## Continuous Integration

The tests are configured to run automatically in the CI/CD pipeline:

1. Unit and integration tests run on every commit
2. Functional tests run on pull requests
3. Smoke tests run after every deployment
4. Performance tests run on a schedule (e.g., nightly)

## Best Practices

1. **Keep tests independent**: Each test should be able to run independently of others
2. **Use appropriate test data**: Create test data that is representative of real-world scenarios
3. **Clean up after tests**: Ensure tests clean up any resources they create
4. **Focus on test coverage**: Aim for high test coverage, especially for critical paths
5. **Maintain tests**: Update tests when the application changes
6. **Use meaningful assertions**: Make assertions that verify the expected behavior
7. **Keep tests fast**: Optimize tests to run quickly to enable rapid feedback

## Troubleshooting

### Common Issues

1. **Tests fail due to database issues**:
   - Ensure TestContainers is properly configured
   - Check that the test database is being initialized correctly

2. **Cucumber tests fail to run**:
   - Verify that the Cucumber dependencies are correctly added to pom.xml
   - Check that the feature files and step definitions are in the correct locations

3. **Performance tests show poor results**:
   - Check for database bottlenecks (missing indexes, inefficient queries)
   - Look for N+1 query problems
   - Verify that caching is properly configured