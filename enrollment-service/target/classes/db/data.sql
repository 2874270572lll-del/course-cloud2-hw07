INSERT IGNORE INTO `courses` (`id`, `code`, `title`, `instructor_id`, `instructor_name`, `instructor_email`,
                    `day_of_week`, `start_time`, `end_time`, `expected_attendance`, `capacity`, `enrolled`)
VALUES
('1', 'CS101', '计算机科学导论', 'T001', '张教授', 'zhang@example.edu.cn',
 'MONDAY', '08:00', '10:00', 50, 60, 0),
('2', 'CS102', '数据结构', 'T002', '李教授', 'li@example.edu.cn',
 'TUESDAY', '10:00', '12:00', 45, 50, 0),
('3', 'CS201', '数据库原理', 'T003', '王教授', 'wang@example.edu.cn',
 'WEDNESDAY', '14:00', '16:00', 40, 45, 0),
('4', 'CS301', 'Web开发技术', 'T004', '赵教授', 'zhao@example.edu.cn',
 'THURSDAY', '16:00', '18:00', 35, 40, 0),
('5', 'CS401', '软件工程', 'T005', '刘教授', 'liu@example.edu.cn',
 'FRIDAY', '09:00', '11:00', 30, 35, 0);

INSERT IGNORE INTO `students` (`id`, `student_id`, `name`, `major`, `grade`, `email`)
VALUES
('S001', '2024001', '张三', '计算机科学与技术', 2024, 'zhangsan@student.edu.cn'),
('S002', '2024002', '李四', '软件工程', 2024, 'lisi@student.edu.cn'),
('S003', '2024003', '王五', '数据科学与大数据技术', 2024, 'wangwu@student.edu.cn'),
('S004', '2024004', '赵六', '计算机科学与技术', 2024, 'zhaoliu@student.edu.cn'),
('S005', '2024005', '孙七', '软件工程', 2024, 'sunqi@student.edu.cn');

INSERT IGNORE INTO `enrollments` (`id`, `course_id`, `student_id`, `status`)
VALUES
('E001', '1', 'S001', 'ACTIVE'),
('E002', '2', 'S002', 'ACTIVE'),
('E003', '3', 'S003', 'ACTIVE');
