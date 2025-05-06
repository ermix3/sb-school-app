# School Management System

A comprehensive School Management System built with Spring Boot, Jakarta EE, and Spring Data JPA, designed to manage
students, teachers, courses, enrollments, and grades in an educational institution.

## 🚀 Features

- **Student Management**: Create, update, retrieve, and search for students
- **Teacher Management**: Track teacher information, specialties, and courses taught
- **Course Management**: Define courses with details like code, title, credits, and capacity
- **Enrollment System**: Manage student enrollments with status tracking
- **Grading System**: Record and manage student grades across different assessment types

## 🛠️ Tech Stack

- **Java 21**: Core programming language
- **Spring Boot**: Application framework
- **Spring Data JPA**: Data access and persistence
- **Spring MVC**: Web layer and RESTful API
- **Lombok**: Reduces boilerplate code
- **Flyway**: Database migration tool
- **MySQL**: Database (configurable)
- **Docker Compose support**: Simplifies running the app with all required services
- **Testcontainers**: Testing framework
- **JUnit 5**: Testing framework

## 📊 Database Schema

The application uses a relational database with the following core tables:

- **students**: Stores student personal information and enrollment details
- **teachers**: Contains teacher information and specialties
- **courses**: Defines course details and associated teachers
- **enrollments**: Junction table managing the many-to-many relationship between students and courses
- **grades**: Records student performance in different assessment types

## 🔧 Setup Instructions

### Prerequisites

- Java 21 or higher
- Maven
- MySQL

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ermix3/sb-school-app.git
   ```

2. Navigate to the project directory:
   ```bash
   cd sb-school-app
   ```

3. Build the project:
   ```bash
   ./mvnw clean install
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080/api/v1/swagger-ui/index.html`

## 🔍 API Endpoints

The system provides RESTful API endpoints for all core functionalities:

- **Students**: `/students` - CRUD operations and search
- **Teachers**: `/teachers` - CRUD operations and search
- **Courses**: `/courses` - CRUD operations and search
- **Enrollments**: `/enrollments` - Manage student course registrations
- **Grades**: `/grades` - Record and retrieve student performance

## 🧪 Testing

The project includes comprehensive testing:

- Unit tests for service and repository layers
- Integration tests for controllers and APIs
- Database migration tests
