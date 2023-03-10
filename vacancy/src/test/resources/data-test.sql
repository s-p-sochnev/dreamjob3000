insert into users (id, username, hashed_password, eligible) values (1, '1', '$2a$11$VdOj/hsL8ao2RJB7dBUPRe5D3CKTRTdGRUJczPt6jFFBB2xt5sRiq', true);

insert into currency (id, code, exchange_rate) values
(1, 'RUR', 1),
(2, 'USD', 60),
(3, 'EUR', 62);

insert into info (last_update_date) values (dateadd('DAY', -2, current_date()));

insert into experience (id, description) values
(1, 'noExperience'),
(2, 'between1And3'),
(3, 'between3And6'),
(4, 'moreThan6');

insert into skill (id, description) values
(1, 'Java'),
(2, 'Spring'),
(3, 'OOP'),
(4, 'Maven'),
(5, 'Spring Boot'),
(6, 'BitBucket'),
(7, 'Agile'),
(8, 'SQL'),
(9, 'Git'),
(10, 'Java EE'),
(11, 'Hibernate'),
(12, 'REST'),
(13, 'SOAP'),
(14, 'Kafka'),
(15, 'JUnit'),
(16, 'AWS'),
(17, 'React'),
(18, 'Kotlin'),
(19, 'SCALA'),
(20, 'Cassandra'),
(21, 'Docker'),
(22, 'Jenkins');

insert into vacancy (id, title, salary_from, salary_to, currency_id, description, experience_id) values
(1, 'Java Junior Developer 1', 100000, 150000, 1, 'Join our company and work as a Java Junior Developer 1 with us!', 1),
(2, 'Java Junior Developer 2', 110000, 160000, 1, 'Join our company and work as a Java Junior Developer 2 with us!', 1),
(3, 'Java Junior Developer 3', 120000, 170000, 1, 'Join our company and work as a Java Junior Developer 3 with us!', 1),
(4, 'Java Junior Developer 4', 130000, 180000, 1, 'Join our company and work as a Java Junior Developer 4 with us!', 2),
(5, 'Java Junior Developer 5', 140000, 190000, 1, 'Join our company and work as a Java Junior Developer 5 with us!', 2),
(6, 'Java Middle Developer 6', 250000, 300000, 1, 'Join our company and work as a Java Middle Developer 6 with us!', 2),
(7, 'Java Middle Developer 7', 260000, 310000, 1, 'Join our company and work as a Java Middle Developer 7 with us!', 3),
(8, 'Java Middle Developer 8', 4000, 4500, 2, 'Join our company and work as a Java Middle Developer 8 with us!', 3),
(9, 'Java Middle Developer 9', 4200, 4800, 2, 'Join our company and work as a Java Middle Developer 9 with us!', 3),
(10, 'Java Middle Developer 10', 4700, 5300, 3, 'Join our company and work as a Java Middle Developer 10 with us!', 3),
(11, 'Java Senior Developer', 290000, 370000, 1, 'Join our company and work as a Java Senior Developer with us!',4),
(12, 'Java Senior Developer', 310000, 390000, 1, 'Join our company and work as a Java Senior Developer with us!', 4),
(13, 'Java Senior Developer', 420000, 490000, 1, 'Join our company and work as a Java Senior Developer with us!', 4),
(14, 'Java Senior Developer', 7000, 7600, 2, 'Join our company and work as a Java Senior Developer with us!', 4),
(15, 'Java Senior Developer', 7500, 8500, 2, 'Join our company and work as a Java Senior Developer with us!', 4);

insert into vacancy_skill_xref (id, vacancy_id, skill_id) values
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 8),
(5, 1, 9),
(6, 2, 1),
(7, 2, 2),
(8, 2, 3),
(9, 2, 8),
(10, 2, 9),
(11, 3, 1),
(12, 3, 2),
(13, 3, 3),
(14, 3, 8),
(15, 3, 9),
(16, 4, 1),
(17, 4, 3),
(18, 4, 4),
(19, 4, 12),
(20, 5, 1),
(21, 5, 3),
(23, 5, 4),
(24, 5, 12),
(25, 6, 1),
(26, 6, 3),
(27, 6, 4),
(28, 6, 12),
(29, 7, 5),
(30, 7, 7),
(31, 7, 8),
(32, 7, 9),
(33, 8, 5),
(34, 8, 7),
(35, 8, 8),
(36, 8, 9),
(37, 9, 5),
(38, 9, 7),
(39, 9, 8),
(40, 9, 9),
(41, 10, 8),
(42, 10, 9),
(43, 10, 11),
(44, 10, 15),
(45, 11, 8),
(46, 11, 9),
(47, 11, 11),
(48, 11, 15),
(49, 12, 8),
(50, 12, 9),
(51, 12, 11),
(52, 12, 15),
(53, 13, 6),
(54, 13, 16),
(55, 13, 19),
(56, 13, 20),
(57, 13, 21),
(58, 13, 22),
(59, 14, 6),
(60, 14, 16),
(61, 14, 19),
(62, 14, 20),
(63, 14, 21),
(64, 14, 22),
(65, 15, 6),
(66, 15, 16),
(67, 15, 19),
(68, 15, 20),
(69, 15, 21),
(70, 15, 22);
