-- Insert sample data into Students table
INSERT INTO students (first_name, last_name, email, date_of_birth, address, phone_number, enrollment_date)
VALUES 
('John', 'Doe', 'john.doe@example.com', '2000-05-15', '123 Main St, Anytown', '555-123-4567', '2022-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2001-08-22', '456 Oak Ave, Somewhere', '555-987-6543', '2022-09-01'),
('Michael', 'Johnson', 'michael.j@example.com', '2000-11-30', '789 Pine Rd, Nowhere', '555-456-7890', '2023-01-15'),
('Emily', 'Williams', 'emily.w@example.com', '2002-03-17', '321 Elm St, Anywhere', '555-789-0123', '2023-01-15'),
('David', 'Brown', 'david.b@example.com', '2001-07-08', '654 Maple Dr, Someplace', '555-234-5678', '2022-09-01');

-- Insert sample data into Teachers table
INSERT INTO teachers (first_name, last_name, email, phone_number, hire_date, subject_specialty)
VALUES 
('Robert', 'Anderson', 'r.anderson@school.edu', '555-111-2222', '2018-08-15', 'Mathematics'),
('Sarah', 'Wilson', 's.wilson@school.edu', '555-333-4444', '2019-07-20', 'English Literature'),
('James', 'Taylor', 'j.taylor@school.edu', '555-555-6666', '2017-06-10', 'Computer Science'),
('Patricia', 'Martinez', 'p.martinez@school.edu', '555-777-8888', '2020-08-01', 'Biology'),
('Thomas', 'Garcia', 't.garcia@school.edu', '555-999-0000', '2021-01-15', 'History');

-- Insert sample data into Courses table
INSERT INTO courses (course_code, title, description, credits, teacher_id, max_students)
VALUES 
('MATH101', 'Introduction to Calculus', 'Fundamental concepts of calculus including limits, derivatives, and integrals.', 4, 1, 30),
('ENG201', 'Advanced Composition', 'Advanced writing techniques and literary analysis.', 3, 2, 25),
('CS150', 'Programming Fundamentals', 'Introduction to programming concepts and problem-solving.', 4, 3, 20),
('BIO110', 'General Biology', 'Introduction to biological principles and concepts.', 4, 4, 30),
('HIST100', 'World History', 'Survey of major historical events and developments around the world.', 3, 5, 35);

-- Insert sample data into Enrollments table
INSERT INTO enrollments (student_id, course_id, enrollment_date, status)
VALUES 
(1, 1, '2022-09-05', 'ACTIVE'),
(1, 3, '2022-09-05', 'ACTIVE'),
(2, 2, '2022-09-06', 'ACTIVE'),
(2, 4, '2022-09-06', 'ACTIVE'),
(3, 3, '2023-01-20', 'ACTIVE'),
(3, 5, '2023-01-20', 'ACTIVE'),
(4, 1, '2023-01-21', 'ACTIVE'),
(4, 4, '2023-01-21', 'ACTIVE'),
(5, 2, '2022-09-07', 'ACTIVE'),
(5, 5, '2022-09-07', 'ACTIVE');

-- Insert sample data into Grades table
INSERT INTO grades (enrollment_id, grade_value, grade_type, comment, date_recorded)
VALUES 
(1, 85.5, 'MIDTERM', 'Good understanding of concepts', '2022-10-15'),
(1, 90.0, 'FINAL', 'Excellent performance', '2022-12-20'),
(2, 78.5, 'MIDTERM', 'Needs improvement in algorithm design', '2022-10-18'),
(2, 82.0, 'FINAL', 'Showed improvement', '2022-12-22'),
(3, 92.0, 'MIDTERM', 'Outstanding analysis', '2022-10-16'),
(3, 94.5, 'FINAL', 'Exceptional work', '2022-12-21'),
(4, 88.0, 'MIDTERM', 'Good lab work', '2022-10-17'),
(4, 91.0, 'FINAL', 'Strong understanding of concepts', '2022-12-19'),
(5, 79.5, 'MIDTERM', 'Needs to improve code organization', '2023-03-10'),
(5, 85.0, 'FINAL', 'Significant improvement', '2023-05-15');