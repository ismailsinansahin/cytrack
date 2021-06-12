insert into batches (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     batch_end_date, batch_start_date, batch_status, name, notes)
values (now(), 1, false, now(), 1, '2021-12-30', '2021-06-30', 'PLANNED', 'EU-1', 'EU only'),
       (now(), 1, false, now(), 1, '2022-12-30', '2021-06-30', 'PLANNED', 'EU-2', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-30', 'PLANNED', 'EU-3', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-30', 'PLANNED', 'EU-4', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-30', 'PLANNED', 'EU-5', 'EU only');

insert into roles (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, description)
values (now(), 1, false, now(), 1, 'Admin'),
       (now(), 1, false, now(), 1, 'Instructor'),
       (now(), 1, false, now(), 1, 'CybertekMentor'),
       (now(), 1, false, now(), 1, 'AlumniMentor'),
       (now(), 1, false, now(), 1, 'Student');

insert into users (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, birthday,
                   country, email, enabled, first_name, gender, last_name, password, phone, status, group_id, role_id)
values (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'mike@ct.com', true, 'Mike', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW', null, 1),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'omer@ct.com', true, 'Omer', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 1),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'ozzy@ct.com', true, 'Ozzy', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 2),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'jamal@ct.com', true, 'Jamal', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 2),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'baha@ct.com', true, 'Baha', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 3),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'cihat@ct.com', true, 'Cihat', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 3),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'osman@ct.com', true, 'Osman', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 4),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'pelin@ct.com', true, 'Pelin', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 4),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'messi@ct.com', true, 'Messi', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 5),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'ronaldo@ct.com', true, 'Ronaldo', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 5),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'iniesta@ct.com', true, 'Iniesta', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 5),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'falcao@ct.com', true, 'Falcao', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 5),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'sneider@ct.com', true, 'Sneider', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 5),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'ribery@ct.com', true, 'Ribery', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 5),
       (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'akbar@ct.com', true, 'Akbar', 'MALE', 'Smith', 'Abc123', '5412589874', 'NEW',null, 2);

insert into lessons (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name, batch_id)
values (now(), 1, false, now(), 1,'Java', 1),
       (now(), 1, false, now(), 1,'Selenium', 1),
       (now(), 1, false, now(), 1,'SDLC', 1),
       (now(), 1, false, now(), 1,'API', 1),
       (now(), 1, false, now(), 1,'Git/GitHub', 1);

insert into lesson_instructor_rel (lesson_id, instructor_id)
values (1, 3),
       (2, 4),
       (3, 3),
       (4, 3),
       (4, 4),
       (1, 15);

insert into groups (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                    mascot, name, alumni_mentor_id, batch_id, cybertek_mentor_id)
values (now(), 1, false, now(), 1, 'Eagles', 'Group-1', 7, 1, 5),
       (now(), 1, false, now(), 1, 'Falcons', 'Group-2', 7, 1, 5),
       (now(), 1, false, now(), 1, 'Tigers', 'Group-3', 8, 1, 6),
       (now(), 1, false, now(), 1, 'Lions', 'Group-4', 8, 1, 6);

insert into tasks (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                   assign_date, due_date, name, type, lesson_id)
values (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'JavaEU1Day01Recording', 'Recording', 1),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Assignment01','Assignment', 1),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Quiz02','Quiz', 1),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day02','Recording', 2),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Assessment-01','Quiz', 2),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day03','Recording', 3),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day04','Recording', 1),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Week01','Flipgrid', 1),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day03','Recording', 2),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Week02','Meeting', 3),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day06','Recording', 3);

insert into task_student_rel (task_id, student_id)
values (1, 9),
       (2, 9),
       (3, 9),
       (4, 9),
       (1, 10),
       (2, 10),
       (3, 10),
       (4, 10),
       (5, 11);

update users set group_id = 1 where users.id between 9 and 14;