CREATE TABLE IF NOT EXISTS `courses` (
    `id` VARCHAR(50) PRIMARY KEY,
    `code` VARCHAR(20) NOT NULL UNIQUE,
    `title` VARCHAR(100) NOT NULL,
    `instructor_id` VARCHAR(20),
    `instructor_name` VARCHAR(50),
    `instructor_email` VARCHAR(100),
    `day_of_week` VARCHAR(20),
    `start_time` VARCHAR(10),
    `end_time` VARCHAR(10),
    `expected_attendance` INT,
    `capacity` INT NOT NULL,
    `enrolled` INT NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `students` (
    `id` VARCHAR(50) PRIMARY KEY,
    `student_id` VARCHAR(20) NOT NULL UNIQUE,
    `name` VARCHAR(50) NOT NULL,
    `major` VARCHAR(100),
    `grade` INT NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `enrollments` (
    `id` VARCHAR(50) PRIMARY KEY,
    `course_id` VARCHAR(50) NOT NULL,
    `student_id` VARCHAR(50) NOT NULL,
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    `enrolled_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `uk_course_student` UNIQUE (`course_id`, `student_id`),
    CONSTRAINT `fk_enrollment_course` FOREIGN KEY (`course_id`) REFERENCES `courses`(`id`),
    CONSTRAINT `fk_enrollment_student` FOREIGN KEY (`student_id`) REFERENCES `students`(`id`)
);
