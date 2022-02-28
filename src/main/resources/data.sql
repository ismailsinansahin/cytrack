insert into batches (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     batch_end_date, batch_start_date, batch_status, name, notes)
values (now(), 1, false, now(), 1, '2021-12-30', '2021-06-30', 'INPROGRESS', 'EU-1', 'EU only'),
       (now(), 1, false, now(), 1, '2022-12-30', '2021-06-26', 'INPROGRESS', 'EU-2', 'EU only'),
       (now(), 1, false, now(), 1, '2020-12-30', '2021-07-27', 'INPROGRESS', 'EU-3', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-28', 'PLANNED', 'EU-4', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-29', 'COMPLETED', 'EU-5', 'EU only');

insert into user_roles (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name)
values (now(), 1, false, now(), 1, 'Admin'),
       (now(), 1, false, now(), 1, 'Instructor'),
       (now(), 1, false, now(), 1, 'Cydeo Mentor'),
       (now(), 1, false, now(), 1, 'Alumni Mentor'),
       (now(), 1, false, now(), 1, 'Student');

insert into users (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, birthday,
                   country, email, enabled, first_name, gender, last_name, password, phone, student_status, group_id, user_role_id, batch_id)
values (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'mike@ct.com', true, 'Mike', 'MALE', 'Black', 'Abc123', '5412589874', null, null, 1, null),
       (now(), 1, false, now(), 1, '1975-01-12', 'TURKEY', 'omer@ct.com', true, 'Omer', 'MALE', 'Bright', 'Abc123', '5325698754', null, null, 2, null),
       (now(), 1, false, now(), 1, '1965-02-23', 'UK', 'ozzy@ct.com', true, 'Ozzy', 'MALE', 'Red', 'Abc123', '5052569847', null, null, 2, null),
       (now(), 1, false, now(), 1, '1986-05-25', 'FRANCE', 'jamal@ct.com', true, 'Jamal', 'MALE', 'Orange', 'Abc123', '5336549855', null, null, 2, null),
       (now(), 1, false, now(), 1, '1981-04-20', 'GERMANY', 'akbar@ct.com', true, 'Akbar', 'MALE', 'Red', 'Abc123', '5054523311', null, null, 2, null),
       (now(), 1, false, now(), 1, '1999-11-01', 'ITALY', 'baha@ct.com', true, 'Baha', 'MALE', 'Darkblue', 'Abc123', '5425213654', null, null, 3, null),
       (now(), 1, false, now(), 1, '2000-10-05', 'NETHERLAND', 'cihat@ct.com', true, 'Cihat', 'MALE', 'Green', 'Abc123', '5423625411', null, null, 3, null),
       (now(), 1, false, now(), 1, '1996-08-30', 'USA', 'osman@ct.com', true, 'Osman', 'MALE', 'White', 'Abc123', '5329885588', null, null, 4, null),
       (now(), 1, false, now(), 1, '1994-07-21', 'UK', 'pelin@ct.com', true, 'Pelin', 'FEMALE', 'Black', 'Abc123', '5302556633', null, null, 4, null),
       (now(), 1, false, now(), 1, '1983-09-16', 'FRANCE', 'messi@ct.com', true, 'Messi', 'MALE', 'Purple', 'Abc123', '5312147789', 'NEW', null, 5, 3),
       (now(), 1, false, now(), 1, '1981-12-08', 'ITALY', 'ronaldo@ct.com', true, 'Ronaldo', 'MALE', 'Pink', 'Abc123', '5423665523', 'NEW', null, 5, 3),
       (now(), 1, false, now(), 1, '1988-11-17', 'USA', 'iniesta@ct.com', true, 'Iniesta', 'MALE', 'Brown', 'Abc123', '5054587895', 'RETURNING', null, 5, 3),
       (now(), 1, false, now(), 1, '2005-01-06', 'TURKEY', 'falcao@ct.com', true, 'Falcao', 'MALE', 'Gray', 'Abc123', '5056669568', 'NEW', null, 5, 3),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'sneider@ct.com', true, 'Sneider', 'MALE', 'Yellow', 'Abc123', '5423124655', 'RETURNING', null, 5, 3),
       (now(), 1, false, now(), 1, '1980-05-10', 'GERMANY', 'ribery@ct.com', true, 'Ribery', 'MALE', 'Blue', 'Abc123', '5325123655', 'NEW', null, 5, 3);

insert into lessons (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name)
values (now(), 1, false, now(), 1,'Java'),
       (now(), 1, false, now(), 1,'Selenium'),
       (now(), 1, false, now(), 1,'SDLC'),
       (now(), 1, false, now(), 1,'API'),
       (now(), 1, false, now(), 1,'Git/GitHub');

insert into instructor_lesson (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, lesson_id, instructor_id)
values (now(), 1, false, now(), 1, 1, 3),
       (now(), 1, false, now(), 1, 2, 4),
       (now(), 1, false, now(), 1, 3, 3),
       (now(), 1, false, now(), 1, 4, 3),
       (now(), 1, false, now(), 1, 4, 4),
       (now(), 1, false, now(), 1, 1, 2),
       (now(), 1, false, now(), 1, 5, 2),
       (now(), 1, false, now(), 1, 5, 3);

insert into groups (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                    name, alumni_mentor_id, batch_id, cydeo_mentor_id)
values (now(), 1, false, now(), 1, 'Group-1', 8, 3, 6),
       (now(), 1, false, now(), 1, 'Group-2', 9, 3, 6),
       (now(), 1, false, now(), 1, 'Group-3', 8, 3, 7),
       (now(), 1, false, now(), 1, 'Group-4', 9, 3, 7);

insert into tasks (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                   publishing_date, due_date, name, task_type, lesson_id, batch_id, task_status)
values (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'JavaEU1Day01Recording', 'RECORDING', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Assignment01','ASSIGNMENT', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Quiz02','QUIZ', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day02','RECORDING', 2, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Assessment-01','QUIZ', 2, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day03','RECORDING', 3, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day04','RECORDING', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Week01','FLIPGRID', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day03','RECORDING', 2, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Week02','WEEKLY_MENTOR_MEETING', 3, 3, 'OUT_OF_TIME'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-12-30', 'Day06','RECORDING', 3, 3, 'PUBLISHED');

-- insert into student_task (task_id, student_id)
-- values (1, 9),
--        (2, 9),
--        (3, 9),
--        (4, 9),
--        (1, 10),
--        (2, 10),
--        (3, 10),
--        (4, 10),
--        (5, 11);
--
-- update users set group_id = 1 where users.id between 10 and 15;